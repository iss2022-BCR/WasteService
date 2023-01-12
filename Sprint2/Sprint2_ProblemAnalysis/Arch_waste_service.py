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
          stop_handler=Custom('stop_handler','./qakicons/symActorSmall.png')
     with Cluster('ctx_transporttrolley', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          pathexecutor_bcr=Custom('pathexecutor_bcr','./qakicons/symActorSmall.png')
     with Cluster('ctx_raspberry', graph_attr=nodeattr):
          sonar_mock_test=Custom('sonar_mock_test','./qakicons/symActorSmall.png')
          alarm_controller=Custom('alarm_controller','./qakicons/symActorSmall.png')
     with Cluster('ctx_robot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
     typesprovider >> Edge(color='green', style='dashed', xlabel='typesreply') >> sys 
     wasteservice >> Edge(color='magenta', style='solid', xlabel='deposit') >> transporttrolley
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadaccepted') >> sys 
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='dopath') >> pathexecutor_bcr
     transporttrolley >> Edge(color='green', style='dashed', xlabel='pickupcompleted') >> sys 
     transporttrolley >> Edge(color='blue', style='solid', xlabel='depositcompleted') >> wasteservice
     transporttrolley >> Edge(color='blue', style='solid', xlabel='depositfailed') >> wasteservice
     pathexecutor_bcr >> Edge(color='magenta', style='solid', xlabel='step') >> basicrobot
     sys >> Edge(color='red', style='dashed', xlabel='startAlarm') >> pathexecutor_bcr
     pathexecutor_bcr >> Edge(color='blue', style='solid', xlabel='cmd') >> basicrobot
     pathexecutor_bcr >> Edge(color='green', style='dashed', xlabel='dopathfail') >> sys 
     pathexecutor_bcr >> Edge(color='green', style='dashed', xlabel='dopathdone') >> sys 
     pathexecutor_bcr >> Edge(color='green', style='dashed', xlabel='dopathfail') >> sys 
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepdone') >> sys 
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepfail') >> sys 
     sonar_mock_test >> Edge(color='blue', style='solid', xlabel='sonar_data') >> alarm_controller
     alarm_controller >> Edge( xlabel='startAlarm', **eventedgeattr) >> sys
     alarm_controller >> Edge( xlabel='stopAlarm', **eventedgeattr) >> sys
diag
