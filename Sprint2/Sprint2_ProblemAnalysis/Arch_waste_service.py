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
with Diagram('waste_serviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          typesprovider=Custom('typesprovider','./qakicons/symActorSmall.png')
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
     with Cluster('ctx_transporttrolley', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          trolleystateprovider=Custom('trolleystateprovider','./qakicons/symActorSmall.png')
          pathexecutorbcr=Custom('pathexecutorbcr','./qakicons/symActorSmall.png')
     with Cluster('ctx_raspberry', graph_attr=nodeattr):
          alarmcontroller=Custom('alarmcontroller','./qakicons/symActorSmall.png')
          ledcontroller=Custom('ledcontroller','./qakicons/symActorSmall.png')
     with Cluster('ctx_robot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
     typesprovider >> Edge(color='green', style='dashed', xlabel='typesreply') >> sys 
     wasteservice >> Edge(color='magenta', style='solid', xlabel='deposit') >> transporttrolley
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadaccepted') >> sys 
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='dopath') >> pathexecutorbcr
     transporttrolley >> Edge(color='green', style='dashed', xlabel='pickupcompleted') >> sys 
     transporttrolley >> Edge(color='blue', style='solid', xlabel='depositcompleted') >> wasteservice
     transporttrolley >> Edge(color='blue', style='solid', xlabel='coapUpdate') >> trolleystateprovider
     pathexecutorbcr >> Edge(color='blue', style='solid', xlabel='coapUpdate') >> trolleystateprovider
     trolleystateprovider >> Edge( xlabel='trolley_state_changed', **eventedgeattr) >> sys
     sys >> Edge(color='red', style='dashed', xlabel='startAlarm') >> pathexecutorbcr
     pathexecutorbcr >> Edge(color='magenta', style='solid', xlabel='step') >> basicrobot
     pathexecutorbcr >> Edge(color='blue', style='solid', xlabel='cmd') >> basicrobot
     pathexecutorbcr >> Edge(color='green', style='dashed', xlabel='dopathdone') >> sys 
     pathexecutorbcr >> Edge(color='green', style='dashed', xlabel='dopathfail') >> sys 
     sys >> Edge(color='red', style='dashed', xlabel='stopAlarm') >> pathexecutorbcr
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepdone') >> sys 
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepfail') >> sys 
     alarmcontroller >> Edge( xlabel='startAlarm', **eventedgeattr) >> sys
     alarmcontroller >> Edge( xlabel='stopAlarm', **eventedgeattr) >> sys
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed') >> ledcontroller
diag
