# Double Booked

When maintaining a calendar of events, it is important to know if an event overlaps with another event. Given a sequence of events, each having a start and end time, write a program that will return the sequence of all pairs of overlapping events.

## Installation

- Install [Leiningen](https://leiningen.org/)
- Run `lein repl` to test things out in the REPL with your own custom inputs
- Run `lein test` to verify with tests I've already written

## Assumptions

While implementing this coding test, I had to make a number of assumptions about edge case behaviour and general data shape. I will capture those assumptions here for review and adjust the behaviour if necessary.

1. General data shape of events
- Assumed the timestamp format for the specified `:start` and `:end` times is ISO 8601
- Included a `:title` field to easily differentiate between events
- Included an `:id` field to easily compare the result
  - Something like this would likely have an ID anyways

2. Edge Cases
- If one event were to end at the exact same time as another started, I assumed they would not be considered overlapping
- While it is specified that all events will have a `:start` and `:end` time, I still wanted to protect against potentially malformed data. I assumed that any malformed events should be ignored
- When given `nil` input, or an empty list of events, I decided to also return `nil`

## Further Reading

I've implemented three different versions of the algorithm, each progressively improved. I thought it would be worthwhile to keep all three versions so you can easily see how my thought process evolved during the challenge.

First I implemented a quick and easy solution by simply comparing all events to each other recursively. This is a nice low-complexity solution, but does not scale particularly well.

I then iterated on this solution by implementing loop/recur to take advantage of tail recursion - however the time complexity of this is still not great ( O(n²) ).

Lastly, I implemented a more efficient solution which first sorts the list by `:start` time, then only compares upcoming events to ones that are still current. Since the list is sorted, we know that if a current event does not conflict with an upcoming event, it won't conflict with any subsequent events, so comparisons are unnecessary.


I did some cursory research to see how others would have implemented this even more efficiently and found this elegant solution online (also written in Clojure!): https://github.com/dhop/double-booked/tree/master

In a real-world scenario, I probably would have just used this pre-existing solution.
