# double-booked

When maintaining a calendar of events, it is important to know if an event overlaps with another event. Given a sequence of events, each having a start and end time, write a program that will return the sequence of all pairs of overlapping events.

## Installation

Install [Leiningen](https://leiningen.org/)
Run `lein repl` to test things out in the REPl with your own custom inputs
Run `lein test` to verify with tests I've already written

## Assumptions

While implementing this coding test, I had to make a number of assumptions about edge case behaviour and general data shape. I will capture those assumptions here for review and adjust the behaviour if necessary.

1. General data shape of events
- Assumed the timestamp format for the specified `:start` and `:end` times is ISO 8601
- Included a `:title` field to easily differentiate between events
- Included an `:id` field to easily compare the results of my functions (something like this would likely have an ID anyways)

2. Edge Cases
- If one event were to end at the exact same time as another started, I assumed they would not be considered overlapping
- While it is specified that all events will have a `:start` and `:end` time, I still wanted to protect against potentially malformed data. I assumed that any malformed events should be ignored
- When given `nil` input, or an empty list of events, I decided to also return `nil`

## Further Reading

This could be implemented more efficiently, however I ended up going with a low-complexity solution. This makes the code easier to maintain, and in a real world setting, I would not want to optomize prematurely.

I did some cursory research to see how others would have implemented this more efficiently and found this elegant solution online (also written in Clojure!): https://github.com/dhop/double-booked/tree/master

If required to implement a more efficient solution, I would likely go with something similar.
