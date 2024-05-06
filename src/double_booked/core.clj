(ns double-booked.core
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

(defn- make-interval
  "Given an event with a start and end time, return a clj-time interval."
  [{:keys [start end] :as _event}]
  (when (and start end)
    (t/interval (f/parse start) (f/parse end))))

(defn- conflicts?
  "Given two events, returns true if their start and end times overlap, and false otherwise."
  [event1 event2]
  (let [interval1 (make-interval event1)
        interval2 (make-interval event2)]
    (when (and interval1 interval2)
      (t/overlaps? interval1 interval2))))

;; Simply compares every event and returns the list of overlapping events
;; Nice and simple, but could be done more efficiently
(defn find-conflicts
  "Given a list of events, return a list of all pairs of events with overlapping times."
  ([events]
   (find-conflicts (first events) (rest events)))
  ([current-event upcoming-events]
   (when (seq upcoming-events)
     (into (keep (fn [next-event]
                   (when (conflicts? current-event next-event)
                     [current-event next-event]))
                 upcoming-events)
           (find-conflicts (first upcoming-events) (rest upcoming-events))))))

;; An improved version of the above, implemented using tail recursion to prevent Stack issues
(defn find-conflicts-improved
  "Given a list of events, return a list of all pairs of events with overlaping times."
  [events]
  (when (seq events)
    (loop [current-event (first events)
           upcoming-events (rest events)
           conflicts []]
      (let [{:keys [start end] :as next-event} (first upcoming-events)]
        (cond
          (nil? next-event) conflicts
          (and start end) (recur (first upcoming-events)
                                 (rest upcoming-events)
                                 (into conflicts
                                       (keep (fn [e]
                                               (when (conflicts? current-event e)
                                                 [current-event e]))
                                             upcoming-events)))
          ;; More events are present, but this one is malformed, so we skip it
          :else (recur (first upcoming-events) (rest upcoming-events) conflicts))))))

;; A more efficient version of the above for large volumes of events
;; Instead of comparing every event, sort the list by :start time and only compare upcoming events to events that are still current
;; Since the list is sorted, we know that if a current event does not conflict with an upcoming event, it won't conflict with any subsequent events
(defn find-conflicts-efficiently
  "Given a list of events, return a list of all pairs of events with overlapping items"
  [events]
  (when (seq events)
    (loop [current-events []
           upcoming-events (sort-by :start events)
           conflicts []]
      (let [{:keys [start end] :as next-event} (first upcoming-events)]
        (cond
          (nil? next-event) conflicts
          (and start end) (let [still-current-events (filter (partial conflicts? next-event) current-events)
                                new-conflicts (mapv (fn [e] [e next-event]) still-current-events)]
                            (recur (conj still-current-events next-event) (rest upcoming-events) (into conflicts new-conflicts)))
          ;; More events are present, but this one is maldormed,
          :else (recur current-events (rest upcoming-events) conflicts))))))