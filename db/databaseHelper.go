package db

import (
	"github.com/boltdb/bolt"
)

const OrgDatabase = "orgDatabase.db"
const WitDatabase = "witDatabase.db"

func OpenDB(dbName string) (*bolt.DB, error) {
	db, err := bolt.Open(dbName, 0600, nil)
	if err != nil {
		return nil, err
	}
	return db, nil
}

/**
 * Functions that writes a pair (key, val) in the bucket "bkt" in the database
 */
func Write(key []byte, val []byte, bkt []byte, database *bolt.DB) error {
	err := database.Update(func(tx *bolt.Tx) error {
		b, err1 := tx.CreateBucketIfNotExists(bkt)
		if err1 != nil {
			return err1
		}
		err2 := b.Put(key, val)
		return err2

	})
	return err
}
