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
with Diagram('test_waste_serviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_wasteservice_test', graph_attr=nodeattr):
          wasteservice_test=Custom('wasteservice_test','./qakicons/symActorSmall.png')
          transporttrolley_test=Custom('transporttrolley_test','./qakicons/symActorSmall.png')
     wasteservice_test >> Edge(color='magenta', style='solid', xlabel='deposit') >> transporttrolley_test
     wasteservice_test >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice_test >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice_test >> Edge(color='green', style='dashed', xlabel='loadaccepted') >> sys 
     transporttrolley_test >> Edge(color='green', style='dashed', xlabel='pickupcompleted') >> sys 
diag
