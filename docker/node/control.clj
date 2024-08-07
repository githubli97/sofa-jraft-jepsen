(defcluster :jraft
  :clients [{:host "n1" :user "admin"}
            {:host "n2" :user "admin"}
            {:host "n3" :user "admin"}
            {:host "n4" :user "admin"}
            {:host "n5" :user "admin"}])

(deftask :date "echo date on cluster"  []
  (ssh "date"))

(deftask :build []
  (local
   (run
     (cd "atomic-server"
         (run
           "lein clean ; lein uberjar"))))
  (local (run "rm atomic-server.tar.gz; tar zcvf atomic-server.tar.gz atomic-server/target atomic-server/control.sh atomic-server/stop.sh atomic-server/test_server.properties")))


(deftask :deploy []
  (scp "atomic-server.tar.gz" "/home/admin/")
  (ssh
     (run
       (cd "/home/admin"
           (run "rm -rf atomic-server/")
           (run "tar zxvf atomic-server.tar.gz")))))
