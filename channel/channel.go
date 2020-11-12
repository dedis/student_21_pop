package channel

import (
	"encoding/json"
	"fmt"
	"github.com/boltdb/bolt"
	"strings"
	"student20_pop/db"
)

/*
 * Function that subscribe a user to a channel. ONLY AT THE PUB/SUB LAYER
 * if user was already subscribed, does nothing
 * does not change LAO's member field
 */
func Subscribe(userId int, channelId []byte) error {

	db, err := db.OpenChannelDB()
	if err != nil {
		return err
	}
	defer db.Close()

	err = db.Update(func(tx *bolt.Tx) error {
		b, err1 := tx.CreateBucketIfNotExists([]byte("sub"))
		if err1 != nil {
			return err1
		}
		var ints []int
		//gets the list of subscribers if exists, converts it to a list of int
		data := b.Get(channelId)
		if data != nil {
			err1 = json.Unmarshal(data, &ints)
			if err1 != nil {
				return err1
			}
		}

		//check if was already susbscribed
		if _, found := Find(ints, userId); found {
			fmt.Println("user was already subscribed")
			return nil
		}
		ints = append(ints, userId)
		//converts []int to string to []byte
		data = []byte(strings.Trim(strings.Join(strings.Split(fmt.Sprint(ints), " "), ","), ""))
		//push values back
		err1 = b.Put(channelId, data)
		return err1
	})

	return err
}

/*
 function that unsubscribes a user from a channel. ONLY AT THE PUB/SUB LAYER
 does nothing if that user was not already subscribed
 does not change LAO's member field
*/
func Unsubscribe(userId int, channelId []byte) error {

	db, err := db.OpenChannelDB()
	if err != nil {
		return err
	}
	defer db.Close()

	err = db.Update(func(tx *bolt.Tx) error {
		b, err1 := tx.CreateBucketIfNotExists([]byte("sub"))
		if err1 != nil {
			return err1
		}
		var ints []int
		//gets the list of subscribers if exists, converts it to a list of int
		data := b.Get(channelId)
		if data != nil {
			err1 = json.Unmarshal(data, &ints)
			if err1 != nil {
				return err1
			}
		}

		//check if was already susbscribed
		i, found := Find(ints, userId)
		if !found {
			fmt.Println("this user was not subscribed to this channel")
			return nil
		}
		//remove elem from array
		ints[i] = ints[len(ints)-1]
		ints = ints[:len(ints)-1]

		//converts []int to string to []byte
		data = []byte(strings.Trim(strings.Join(strings.Split(fmt.Sprint(ints), " "), ","), ""))
		//push values back
		err1 = b.Put(channelId, data)
		return err1
	})

	return err
}

/* helper function to find an elem. in a slice */
func Find(slice []int, val int) (int, bool) {
	for i, item := range slice {
		if item == val {
			return i, true
		}
	}
	return -1, false
}

func GetSubscribers(channel []byte) []int {
	return nil
}
