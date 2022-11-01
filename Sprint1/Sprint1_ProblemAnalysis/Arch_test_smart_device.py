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
with Diagram('test_smart_deviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_smartdevice', graph_attr=nodeattr):
          smartdevice_simulator=Custom('smartdevice_simulator','./qakicons/symActorSmall.png')
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          typesprovider=Custom('typesprovider(ext)','./qakicons/externalQActor.png')
          wasteservice=Custom('wasteservice(ext)','./qakicons/externalQActor.png')
     smartdevice_simulator >> Edge(color='magenta', style='solid', xlabel='typesrequest') >> typesprovider
     smartdevice_simulator >> Edge(color='magenta', style='solid', xlabel='storerequest') >> wasteservice
diag
