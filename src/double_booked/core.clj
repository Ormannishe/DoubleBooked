(ns double-booked.core
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

;; Double Booked
;;
;; When maintaining a calendar of events, it is important to know if an event overlaps with another event.
;; Given a sequence of events, each having a start and end time,
;; write a program that will return the sequence of all pairs of overlapping events.

;; TODO: Put these into proper tests
(def sample-events
  [{:id 1
    :title "Litera All Staff Meeting"
    :start "2024-05-03T10:00:00.000Z"
    :end "2024-05-03T11:00:00.000Z"}
   {:id 2
    :title "James / Karen BiWeekly 1:1"
    :start "2024-05-03T10:15:00.000Z"
    :end "2024-05-03T11:45:00.000Z"}
   {:id 3
    :title "OOO Dentist"
    :start "2024-05-03T00:00:00.000Z"
    :end "2024-05-04T00:00:00.000Z"}
   {:id 4
    :title "Quick Sync Up"
    :start "2024-05-03T10:00:00.000Z"
    :end "2024-05-03T10:15:00.000Z"}
   {:id 5
    :title "Lunch"
    :start "2024-05-03T11:00:00.000Z"
    :end "2024-05-03T12:00:00.000Z"}
   {:id 6
    :title "Cottage Weekend"
    :start "2024-05-06T00:00:00.000Z"
    :end "2024-05-08T00:00:00.000Z"}])

(defn- make-interval
  "Given an event with a start and end time, return a clj-time interval."
  [{:keys [start end] :as _event}]
  (when (and start end)
    (t/interval (f/parse start) (f/parse end))))

(defn- conflicts?
  "Given two events, returns true if their start and end times overlap, and false otherwise."
  [event1 event2]
  (when (and event1 event2)
    (t/overlaps? (make-interval event1)
                 (make-interval event2))))

;; TODO: Might as well implement the more efficient solution as well ?
;;
;; First pass, kind of dumb solution
;; Simply compares every event and returns the list of overlapping events
;; Nice and simple, but could be done more efficiently
(defn find-conflicts
  "Given a list of events, return a list of all pairs of events with overlaping times."
  ([events]
   (when (seq events)
     (find-conflicts (first events) (rest events))))
  ([current-event upcoming-events]
   (when (seq upcoming-events)
     (concat (keep (fn [next-event]
                     (when (conflicts? current-event next-event)
                       [current-event next-event]))
                   upcoming-events)
             (find-conflicts (first upcoming-events) (rest upcoming-events))))))
