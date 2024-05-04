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
  "Given a list of events, return a list of all pairs of events with overlaping times."
  ([events]
   (find-conflicts (first events) (rest events)))
  ([current-event upcoming-events]
   (when (seq upcoming-events)
     (concat (keep (fn [next-event]
                     (when (conflicts? current-event next-event)
                       [current-event next-event]))
                   upcoming-events)
             (find-conflicts (first upcoming-events) (rest upcoming-events))))))
