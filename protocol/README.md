# student_21_pop: Protocol Specifications
Proof-of-personhood, Spring 2021: Protocol specifications


_Most of the sub-folders have their own README detailing some general information about their layer._

Every "type" : "object" is described in more details in a sub-folder.

There might a way to avoid repeating all the wrapping information by using refs, but for now, it may be safer to just repeat it so that every json file is guaranteed to be complete.

# Introduction
The Personhood.Online system will communicate over WebSockets and rely on a Publish/Subscribe communication pattern, to support the Personhood.Online notion of “Communication channels” described in Data Pipeline architecture specification.
As WebSockets do not naturally support Publish/Subscribe, a low-level protocol is described to provide the Publish/Subscribe communication layer.
Building on top of this low-level protocol, a high-level protocol will provide the Personhood.Online application-level communication. This protocol is also described here.

## Validation and Disambiguation
To make sure that the protocol is understood by everyone equally and to ensure that it is implemented correctly, all messages will be described using the JSON Schema (proposed IETF standard). This will enable the teams to validate their inputs and outputs with the schema files as part of their testing strategy.
The JSON Schema description is not part of this document and will be provided in a dedicated branch of the project’s Github repository.

## Representation of complex data types in the protocol

base64: base64 in string format  
Public Key: base64  
Signature: base64  
Hash: base64  
Timestamp: uint64 representation of the Unix timestamp (seconds since January 1st, 1970)  

## Concatenation for hashing
When concatenating strings for hashing, the following logic is applied:

<code>hash(a<sub>1</sub>,a<sub>2</sub>,...,a<sub>n</sub>) = hash( string(length(a<sub>1</sub>)) || a<sub>1</sub> || string(length(a<sub>2</sub>)) || a<sub>2</sub> || ... || string(length(a<sub>n</sub>)) || a<sub>n</sub>)</code>

where <code>a<sub>1</sub>, ..., a<sub>n</sub></code> are UTF-8 strings, `length()` computes the length in bytes of the UTF-8 string, `string()` is the textual representation of a number and `||` represents the concatenation.
