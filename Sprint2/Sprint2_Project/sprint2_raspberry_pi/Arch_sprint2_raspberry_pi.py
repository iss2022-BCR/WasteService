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
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          typesprovider=Custom('typesprovider(ext)','./qakicons/externalQActor.png')
          wasteservice=Custom('wasteservice(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_transporttrolley', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley(ext)','./qakicons/externalQActor.png')
          trolleystateprovider=Custom('trolleystateprovider(ext)','./qakicons/externalQActor.png')
          pathexecutorbcr=Custom('pathexecutorbcr(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_robot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_raspberrypi', graph_attr=nodeattr):
          sonar_bcr=Custom('sonar_bcr','./qakicons/symActorSmall.png')
          led_bcr=Custom('led_bcr','./qakicons/symActorSmall.png')
          textdisplay_bcr=Custom('textdisplay_bcr','./qakicons/symActorSmall.png')
          buzzer_bcr=Custom('buzzer_bcr','./qakicons/symActorSmall.png')
          filter_distancechanged=Custom('filter_distancechanged(coded)','./qakicons/codedQActor.png')
          filter_distancebounds=Custom('filter_distancebounds(coded)','./qakicons/codedQActor.png')
          filter_textdisplay=Custom('filter_textdisplay(coded)','./qakicons/codedQActor.png')
          filter_alarm=Custom('filter_alarm(coded)','./qakicons/codedQActor.png')
          sonarinput=Custom('sonarinput(coded)','./qakicons/codedQActor.png')
     sonar_bcr >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonarinput
     sonar_bcr >> Edge(color='blue', style='solid', xlabel='sonardeactivate', fontcolor='blue') >> sonarinput
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed', fontcolor='red') >> led_bcr
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed', fontcolor='red') >> textdisplay_bcr
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed', fontcolor='red') >> buzzer_bcr
diag
