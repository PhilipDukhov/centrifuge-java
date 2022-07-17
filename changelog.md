0.2.0
=====

This release adopts a new iteration of Centrifugal protocol and a new iteration of API. Client now behaves according to the client [SDK API specification](https://centrifugal.dev/docs/transports/client_api). The work has been done according to [Centrifugo v4 roadmap](https://github.com/centrifugal/centrifugo/issues/500).

Check out [Centrifugo v4 release post](https://centrifugal.dev/blog/2022/07/19/centrifugo-v4-released) that covers the reasoning behind changes.

All the current core features of Centrifugal client protocol are now supported here.  

New release only works with Centrifugo >= v4.0.0 and [Centrifuge](https://github.com/centrifugal/centrifuge) >= 0.25.0. See [Centrifugo v4 migration guide](https://centrifugal.dev/docs/getting-started/migration_v4) for details about the changes in the ecosystem.

Note, that Centrifugo v4 supports clients working over the previous protocol iteration, so you can update Centrifugo to v4 without any changes on the client side (but you need to turn on `use_client_protocol_v1_by_default` option in the configuration of Centrifugo, see Centrifugo v4 migration guide for details).

0.1.0
=====

Update to work with Centrifuge >= v0.18.0 and Centrifugo v3.

**Breaking change:** client History API behavior changed – Centrifuge >= v0.18.0 and Centrifugo >= v3.0.0 won't return all publications in a stream by default, see Centrifuge [v0.18.0 release notes](https://github.com/centrifugal/centrifuge/releases/tag/v0.18.0) or [Centrifugo v3 migration guide](https://centrifugal.dev/docs/getting-started/migration_v3) for more information and workaround on server-side. History call now accepts options. Example on how to mimic previous behavior:

```
HistoryOptions opts = new HistoryOptions.Builder().withLimit(-1).build();
subscription.history(opts, ...)
```

If you are using Centrifuge < v0.18.0 or Centrifugo v2 then default options will work the same way as before - i.e. return all publications in a history stream.

* Protocol definitions updated to the latest version
* Support for top-level `presence`, `presenceStats` and `history` methods
* When working with Centrifugo v3 or Centrifuge >= v0.18.0 it's now possible to avoid using `?format=protobuf` in connection URL. Client will negotiate Protobuf protocol with a server using WebSocket subprotocol mechanism (in request headers).

0.0.8
=====

* Add possibility to connect over proxy - [#26](https://github.com/centrifugal/centrifuge-java/pull/26)

0.0.7
=====

* attempt to fix JDK version problems

0.0.6
=====

* Support message recovery
* Support server-side subscriptions
* Migrate to protobuf-javalite
* Pass exception to ErrorEvent
* Print stack trace in case of connect error

0.0.5
=====

* Support RPC `method` field - [#16](https://github.com/centrifugal/centrifuge-java/pull/16)

0.0.4
=====

* add `Data` field for `ConnectEvent`

0.0.3
=====

* fix executing Subscription methods in Subscribed state - see [#9](https://github.com/centrifugal/centrifuge-java/issues/9)

0.0.2
=====

Change Subscription API a bit – users should first create new Subscription then manage it's lifecycle (subscribe, unsubscribe) until done with it. When done it's possible to remove subscription using `Client.removeSubscription` method.

0.0.1
=====

Initial release.
