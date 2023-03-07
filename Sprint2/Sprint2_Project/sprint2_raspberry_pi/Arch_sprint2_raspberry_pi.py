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
          alarmcontroller=Custom('alarmcontroller','./qakicons/symActorSmall.png')
          ledcontroller=Custom('ledcontroller','./qakicons/symActorSmall.png')
     alarmcontroller >> Edge( xlabel='stop', **eventedgeattr, fontcolor='red') >> sys
     alarmcontroller >> Edge( xlabel='resume', **eventedgeattr, fontcolor='red') >> sys
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed', fontcolor='red') >> ledcontroller
diag
