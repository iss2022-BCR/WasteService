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
with Diagram('test_defect_solutionArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_defect_solution_test', graph_attr=nodeattr):
          typesprovider=Custom('typesprovider','./qakicons/symActorSmall.png')
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
          transporttrolley_ds_test=Custom('transporttrolley_ds_test','./qakicons/symActorSmall.png')
     typesprovider >> Edge(color='green', style='dashed', xlabel='typesreply') >> sys 
     wasteservice >> Edge(color='blue', style='solid', xlabel='deposit') >> transporttrolley_ds_test
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadrejected') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadaccepted') >> sys 
     transporttrolley_ds_test >> Edge(color='blue', style='solid', xlabel='pickupcompleted') >> wasteservice
     transporttrolley_ds_test >> Edge(color='blue', style='solid', xlabel='depositcompleted') >> wasteservice
diag
