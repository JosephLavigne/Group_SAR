# Message queues specification

## Overview

This library provides an event-based message system to communicate between tasks. It builds on top of the [channels layer](specification_channels.md), adding packet-like communication (messages) instead of streams (channels).  
Like the channels layer, it uses tasks, brokers, and channels, and follows an asynchronous event-based design without multithreading.

However, it is **not** designed for interoperability between the two layers: the case of writing messages at one end of a `MessageQueue` and reading a byte stream from the other end (or vice-versa) is not supported. `QueueBroker`s connect to other `QueueBroker`s and not to `Broker`s.

## API

### QueueBroker
The QueueBroker is the equivalent of the Broker for the message queue layer. It handles port binding and rendezvous to establish connections.

```java
boolean bind(int port, QueueAcceptListener listener);
```
Accept connections on the given `port` of this broker. When a connection is established, i.e. when another task calls `QueueBroker.connect()` on the same port, the `QueueAcceptListener`'s `accepted()` method is called, providing the `MessageQueue` between the two tasks.  
If another task is already bound to the same port on this broker, `bind()` fails and returns `false`.


```java
boolean unbind(int port);
```
Unbind from the given `port`, which becomes free for another task (or the same) to bind to. If the task calling `unbind()` is not bound to this port, it fails and returns `false`.

```java
boolean connect(String name, int port, QueueConnectListener listener);
```
Request a connection to the given `port` of the broker named `name`. When a connection is established, the `QueueConnectListener` is called back, either through the `connected()` method (which provides the `MessageQueue` between the two tasks), or the `refused()` method, indicating that no task is currently accepting to this port (no task present, or a message queue already established).  
If no broker exists with this name, `connect()` fails and returns `false`. 


### QueueChannel
The message queue object, equivalent to a `Channel` for this layer.

```java
void setListener(QueueChannelListener listener);
```
Set the read listener for the channel. When a message is received, the listener will be notified with the `receive()` method.


```java
boolean send(Message message);
```
Send the given Message. Returns false if it cannot be sent, for any reason.

```java
void close();
```
Closes the channel; after this, calling `send()` is no longer allowed.  
N.B: the remote task will not see the channel as closed until it has read all available messages (to avoid those messages being dropped). 

```java
boolean isClosed();
```
Returns `true` if this task has called `close()` on the channel, or if the remote task closed it and all incoming messages have been read (i.e. there are no messages in transit).