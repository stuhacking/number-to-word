;;; Num 2 Word Build
;;
;; SPDX-FileCopyrightText: 2026 Stu Hacking <stuhacking@gmail.com>
;; SPDX-License-Identifier: MIT

;;; Code:
(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'stuhacking/num2word)
(def version "1.0.0")
(def class-dir "target/classes")
(def jar-file (format "target/%s-%s.jar" (name lib) version))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))


(defn clean [_]
  (b/delete {:path "target"}))


(defn jar [_]
  (println "Copying sources...")
  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (println "Compiling clojure...")
  (b/compile-clj {:basis @basis
                  :ns-compile '[num2word.main]
                  :class-dir class-dir})
  (println "Creating jar...")
  (b/jar {:class-dir class-dir
          :jar-file jar-file
          :basis @basis
          :main 'num2word.main}))


(defn uber [_]
  (println "Copying sources...")
  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (println "Compiling clojure...")
  (b/compile-clj {:basis @basis
                  :ns-compile '[num2word.main]
                  :class-dir class-dir})
  (println "Creating jar...")
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'num2word.main}))
