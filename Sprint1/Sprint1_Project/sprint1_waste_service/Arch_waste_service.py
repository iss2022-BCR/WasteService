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
     with Cluster('ctx_smartdevice', graph_attr=nodeattr):
          smartdevice_simulator=Custom('smartdevice_simulator','./qakicons/symActorSmall.png')
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          typesprovider=Custom('typesprovider','./qakicons/symActorSmall.png')
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          pathexecutor=Custom('pathexecutor','./qakicons/symActorSmall.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
     smartdevice_simulator >> Edge(color='magenta', style='solid', xlabel='typesrequest', fontcolor='magenta') >> typesprovider
     smartdevice_simulator >> Edge(color='magenta', style='solid', xlabel='storerequest', fontcolor='magenta') >> wasteservice
     wasteservice >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexecutor
     transporttrolley >> Edge(color='blue', style='solid', xlabel='depositcompleted', fontcolor='blue') >> wasteservice
     pathexecutor >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
     pathexecutor >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
     sys >> Edge(color='red', style='dashed', xlabel='alarm', fontcolor='red') >> pathexecutor
diag
