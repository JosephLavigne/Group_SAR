# Channels specification

## Overview
This library provides an event-based communication interface between tasks.

Tasks are asynchronous, and may or may not run on the same machine. They use channels to exchange data.

A channel is a bidirectional stream between exactly two tasks. Each task can send and receive bytes to/from the channel independently (full-duplex). A channel is also 
- FIFO (bytes are received in the order they are sent)
- lossless (bytes are never lost in transit)

Brokers are the intermediaries through which tasks can find other tasks and establish channels. They are identified by a name, unique across all instances.  
Each broker also has its own namespace of port numbers, which are unique *for this broker*. A `(broker name, port number)` pair identifies a single channel, to which two tasks may connect: one task requests a connection, the other accepts it, in no particular order.

Each task has a default broker, available through `getBroker()`, which it can use to make connections.

---

The library follows an event-based design. Methods are never blocking, but instead take a Listener argument and return right away. When the work is done, the listener is called with the results of the call (if any).  
All events are processed on the same thread ; this library *not* designed for concurrent access.

## API
### Broker

Multiple tasks may use the same broker simultaneously. Each port number, however, is unique for this broker, and only two tasks may use it at once -- one accepting and one connecting.

```java
Broker(String name); # constructor
```
The `name` is a unique identifier, used to find the broker. Attempting to create two brokers with the same name will fail.
 
```java
boolean bind(int port, AcceptListener listener);
```
Accept connections on the given `port` of this broker. When a connection is established, i.e. when another task calls `Broker.connect()` on the same port, the `AcceptListener`'s `accepted()` method is called, providing the `Channel` between the two tasks.  
If another task is already bound to the same port on this broker, `bind()` fails and returns `false`.

```java
boolean unbind(int port);
```
Unbind from the given `port`, which becomes free for another task (or the same) to bind to. If the task calling `unbind()` is not bound to this port, it fails and returns `false`.

```java
boolean connect(String name, int port, ConnectListener listener);
```
Request a connection to the given `port` of the broker named `name`. When a connection is established, the `ConnectListener` is called back, either through the `connected()` method (which provides the `Channel` between the two tasks), or the `refused()` method, indicating that no task is currently accepting to this port (no task present, or a channel already established).  
If no broker exists with this name, `connect()` fails and returns `false`. 


### Channel

```java
void setReadListener(ReadListener readListener);
```
Set the ReadListener for the channel. When data is available to be read from the channel, the listener will be notified with the `available()` method.


```java
int read(byte[] bytes, int offset, int length) throws ChannelDisconnectedException;
```
Attempts to read `length` bytes from the channel into the `bytes` array starting at `offset`. Returns the number of bytes actually read (may be zero). Throws ChannelDisconnectedException if the channel is disconnected.


```java
boolean write(byte[] bytes, int offset, int length, WriteListener writeListener) throws ChannelDisconnectedException;
```
Attempts to write `length` bytes from the `bytes` array, starting at `offset`, to the channel. When the write is done, the `WriteListener` will be notified of the number of bytes written with the `writtenBytes()` method. Returns false if the write operation cannot begin, for any reason. Throws ChannelDisconnectedException if the channel is disconnected.


```java
void disconnect(DisconnectListener disconnectListener);
```
Disconnects the channel, and notifies the `DisconnectListener` when complete. After this, calling `read()` or `write()` is no longer allowed.  
N.B: the remote task will not see the channel as disconnected until it has read all available bytes (to avoid those bytes being dropped). 

```java
boolean disconnected();
```
Returns `true` if this task has called `disconnect()` on the channel, or if the remote task disconnected and all incoming bytes have been read (i.e. there are no bytes in transit).

### Task

A task provides both a logical separation between functional components (e.g. a reader task, writer task...), and an interface with the event system. The user can post events on the task with `postRunnable()`, which will be scheduled and executed along with the internal events.
