package hub

import (
	"log"
	"student20_pop/message"
	"sync"

	"go.dedis.ch/kyber/v3"
)

// witnessHub represents a witness server. A witness server establishes a connection to each
// other server (both the organizer and all other witnesses) and to each client. Each message
// received must be signed by the witness and sent to all other servers (or just witnesses?)
// and to all clients. (check if correct)
type witnessHub struct {
	messageChan chan IncomingMessage

	sync.RWMutex
	channelByID map[string]Channel

	public kyber.Point
}

func NewWitnessHub(public kyber.Point) Hub {
	return &witnessHub{
		messageChan: make(chan IncomingMessage),
		channelByID: make(map[string]Channel),
		public:      public,
	}
}

func (w *witnessHub) Start(done chan struct{}) {
	return
}

func (w *witnessHub) Recv(msg IncomingMessage) {
	log.Printf("witnessHub::Recv")
	w.messageChan <- msg
	// call routine to sign message and send it on here or elsewhere?
}

func (w *witnessHub) RemoveClient(client *Client) {
	w.RLock()
	defer w.RUnlock()

	for _, channel := range w.channelByID {
		channel.Unsubscribe(client, message.Unsubscribe{})
	}

	return
}
