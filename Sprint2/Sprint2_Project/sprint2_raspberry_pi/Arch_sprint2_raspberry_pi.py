from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('sprint2_raspberry_piArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_raspberrypi', graph_attr=nodeattr):
          sonar_bcr=Custom('sonar_bcr','./qakicons/symActorSmall.png')
          testalarmreceiver=Custom('testalarmreceiver','./qakicons/symActorSmall.png')
          led_bcr=Custom('led_bcr','./qakicons/symActorSmall.png')
          led_test=Custom('led_test','./qakicons/symActorSmall.png')
          sonardatasource_concrete=Custom('sonardatasource_concrete(coded)','./qakicons/codedQActor.png')
          sonardatasource_mock=Custom('sonardatasource_mock(coded)','./qakicons/codedQActor.png')
          filter_distancechanged=Custom('filter_distancechanged(coded)','./qakicons/codedQActor.png')
          filter_distancebounds=Custom('filter_distancebounds(coded)','./qakicons/codedQActor.png')
          filter_alarm=Custom('filter_alarm(coded)','./qakicons/codedQActor.png')
     sonar_bcr >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonardatasource_concrete
     sonar_bcr >> Edge(color='blue', style='solid', xlabel='sonardeactivate', fontcolor='blue') >> sonardatasource_concrete
     sys >> Edge(color='red', style='dashed', xlabel='stop', fontcolor='red') >> testalarmreceiver
     sys >> Edge(color='red', style='dashed', xlabel='resume', fontcolor='red') >> testalarmreceiver
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed', fontcolor='red') >> led_bcr
diag
