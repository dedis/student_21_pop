package hub

import (
	"encoding/json"
	"log"
	"student20_pop/message"
	"sync"
	"time"

	"github.com/gorilla/websocket"
	"golang.org/x/xerrors"
)

const (
	// maxMessageSize denotes a meximum possible message size in bytes
	maxMessageSize = 256 * 1024

	writeWait = 10 * time.Second

	pongWait = 60 * time.Second

	pingPeriod = (pongWait * 9) / 10
)

// Client represents a client connected to the server.
type Client struct {
	hub Hub

	conn *websocket.Conn

	send chan []byte

	Wait sync.WaitGroup
}

// NewClient returns an instance of a Client.
func NewClient(h Hub, conn *websocket.Conn) *Client {
	return &Client{
		hub:  h,
		conn: conn,
		send: make(chan []byte, 256),
		Wait: sync.WaitGroup{},
	}
}

// ReadPump starts the reader loop for the client.
func (c *Client) ReadPump() {
	defer func() {
		c.conn.Close()
		c.Wait.Done()
	}()

	c.Wait.Add(1)

	log.Printf("listening for messages from client")

	c.conn.SetReadLimit(maxMessageSize)
	c.conn.SetReadDeadline(time.Now().Add(pongWait))
	c.conn.SetPongHandler(func(string) error { c.conn.SetReadDeadline(time.Now().Add(pongWait)); return nil })
	for {
		_, message, err := c.conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				log.Printf("connection dropped unexpectedly: %v", err)
			}
			break
		}

		// TODO: validate message using JSON schema and unmarshal it to generic
		// message at this stage

		msg := IncomingMessage{
			Client:  c,
			Message: message,
		}

		c.hub.Recv(msg)
	}
}

// WritePump starts the writer loop for the client.
func (c *Client) WritePump() {
	ticker := time.NewTicker(pingPeriod)
	defer func() {
		ticker.Stop()
		c.conn.Close()
		c.Wait.Done()
	}()

	c.Wait.Add(1)

	for {
		select {
		case message, ok := <-c.send:
			c.conn.SetWriteDeadline(time.Now().Add(writeWait))
			if !ok {
				c.conn.WriteMessage(websocket.CloseMessage, []byte{})
				return
			}

			w, err := c.conn.NextWriter(websocket.TextMessage)
			if err != nil {
				log.Printf("failed to retrieve writer: %v", err)
				return
			}

			w.Write(message)

			if err := w.Close(); err != nil {
				log.Printf("failed to close writer: %v", err)
				return
			}
		case <-ticker.C:
			c.conn.SetWriteDeadline(time.Now().Add(writeWait))
			if err := c.conn.WriteMessage(websocket.PingMessage, nil); err != nil {
				log.Printf("failed to send ping: %v", err)
				return
			}
		}
	}
}

// Send allows sending a serialised message to the client.
func (c *Client) Send(msg []byte) {
	log.Printf("sending message to %s", c.conn.RemoteAddr())
	c.send <- msg
}

// SendError is a utility method that allows sending an `error` as a `message.Error`
// message to the client.
func (c *Client) SendError(id int, err error) {
	msgError := &message.Error{}

	if xerrors.As(err, &msgError) {
		answer := message.Answer{
			ID:    &id,
			Error: msgError,
		}

		answerBuf, err := json.Marshal(answer)
		if err != nil {
			log.Printf("failed to marshal answer: %v", err)
		}

		c.send <- answerBuf
	}
}

// SendResult is a utility method that allows sending a `message.Result` to the client.
func (c *Client) SendResult(id int, res message.Result) {
	answer := message.Answer{
		ID:     &id,
		Result: &res,
	}

	answerBuf, err := json.Marshal(answer)
	if err != nil {
		log.Printf("failed to marshal answer: %v", err)
	}

	log.Printf("answerBuf: %s, received id: %d", answerBuf, id)
	c.send <- answerBuf
}
