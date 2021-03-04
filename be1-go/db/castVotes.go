package db

import (

)

func sendVote(laoId string, electionId string, database string, vote string) error{
	// TODO : check if laoId and electionId can be inferred
	channelId := []byte("/root/"+ laoId +"/"+ electionId)
	electionChannel := GetChannel(channelId, database)
	//Data should be in the channel, once the users are
	// subscribed they can see the data written inside it by
	//a LAO member ----> it's response
	//need somehow send a message through this electionChannel to cast
	//a given votes
	/* TODO : create a message that will represent the vote for the
	given question with write-in being disabled  */

}
