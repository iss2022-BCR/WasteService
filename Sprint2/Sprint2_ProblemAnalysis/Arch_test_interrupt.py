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
with Diagram('test_interruptArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_interrupt_test', graph_attr=nodeattr):
          user=Custom('user','./qakicons/symActorSmall.png')
          interruptcaller=Custom('interruptcaller','./qakicons/symActorSmall.png')
          test=Custom('test','./qakicons/symActorSmall.png')
     user >> Edge(color='magenta', style='solid', xlabel='requestdeposit') >> test
     interruptcaller >> Edge(color='blue', style='solid', xlabel='stop') >> test
     interruptcaller >> Edge(color='blue', style='solid', xlabel='resume') >> test
     test >> Edge(color='green', style='dashed', xlabel='accepted') >> sys 
     test >> Edge(color='green', style='dashed', xlabel='rejected') >> sys 
diag
