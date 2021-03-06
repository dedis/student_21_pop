package db

// This file contains functions used to deal with messages in the database. Like create/update a channel and
//get a message in particular.

import (
	"encoding/json"
	"log"
	"student20_pop/message"

	"github.com/boltdb/bolt"
	"golang.org/x/xerrors"
)

// writeMessage  writes a message to the database. The argument "creating" is to specify whether or not
// we want to update a message, which means overwriting existing data
func writeMessage(message message.Message, channel string, database string, creating bool) error {
	db, err := OpenDB(database)
	if err != nil {
		return xerrors.Errorf("failed to open db: %v", err)
	}
	defer db.Close()

	err = db.Update(func(tx *bolt.Tx) error {
		b, err := tx.CreateBucketIfNotExists([]byte(channel))
		if err != nil {
			return xerrors.Errorf("failed to create bucket: %v", err)
		}

		check := b.Get(message.MessageID)

		if check != nil && creating {
			return errorKeyExists
		}
		if check == nil && !creating {
			return errorInvalidResource
		}

		// Marshal the message and store it
		msg, err := json.Marshal(message)
		if err != nil {
			return errorInvalidData
		}
		err = b.Put(message.MessageID, msg)
		if err != nil {
			return xerrors.Errorf("failed to put message: %v", err)
		}

		return nil
	})

	return err
}

//CreateMessage stores a new message in the database. It returns an error if a message with the same ID already existed
// in the same channel. It just calls writeMessage with creating = true
func CreateMessage(message message.Message, channel string, database string) error {
	return writeMessage(message, channel, database, true)
}

// UpdateMessage overwrites a message in the database. It returns an error if there was no message with the same ID in
// that channel. It just calls writeMessage with creating = false
func UpdateMessage(message message.Message, channel string, database string) error {
	return writeMessage(message, channel, database, false)
}

// GetMessage returns the content of a message sent on a channel. Nil if the message, channel or DB does not exist
func GetMessage(channel []byte, message []byte, database string) []byte {
	db, err := OpenDB(database)
	if err != nil {
		return nil
	}
	defer db.Close()

	var result []byte
	err = db.View(func(tx *bolt.Tx) error {
		b := tx.Bucket(channel)
		if b == nil {
			log.Printf("Could not find bucket with corresponding channel ID in GetMessage()")
			return errorInvalidResource
		}

		data := b.Get(message)
		result = make([]byte, len(data))
		copy(result, data)
		return nil
	})

	if err != nil || len(result) == 0 {
		return nil
	}
	return result
}
