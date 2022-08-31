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
with Diagram('demo_system_overview_v0Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctx_monitor', graph_attr=nodeattr):
          wasteservicestatusgui=Custom('wasteservicestatusgui','./qakicons/symActorSmall.png')
     with Cluster('ctx_raspdevice', graph_attr=nodeattr):
          alarmdevice=Custom('alarmdevice','./qakicons/symActorSmall.png')
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
     with Cluster('ctx_smartdevice_test', graph_attr=nodeattr):
          smartdevice_test=Custom('smartdevice_test','./qakicons/symActorSmall.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     smartdevice_test >> Edge(color='magenta', style='solid', xlabel='storerequest') >> wasteservice
     wasteservice >> Edge(color='blue', style='solid', xlabel='doDeposit') >> transporttrolley
     wasteservice >> Edge(color='blue', style='solid', xlabel='updategui') >> wasteservicestatusgui
     wasteservice >> Edge(color='blue', style='solid', xlabel='stop') >> transporttrolley
     wasteservice >> Edge(color='blue', style='solid', xlabel='resume') >> transporttrolley
     transporttrolley >> Edge(color='blue', style='solid', xlabel='updateled') >> warningdevice
     transporttrolley >> Edge(color='blue', style='solid', xlabel='updategui') >> wasteservicestatusgui
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='step') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='cmd') >> basicrobot
     alarmdevice >> Edge(color='blue', style='solid', xlabel='distance') >> wasteservice
diag
