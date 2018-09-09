# MapOptics
MapOptics is a lightweight cross-platform tool that enables the user to visualise and interact with the alignment of Bionano optical mapping data and can be used for in depth exploration of hybrid scaffolding alignments.

# Loading maps
Maps can be loaded under File > Load Maps. Here, the program asks for an “XMAP file”, “Reference CMAP file” and “Query CMAP file”. These files are outputted at various stages of hybrid scaffolding to store information about alignment. As long as the three maps are within the same dataset, they should be visualised with ease. This could be for the display of alignment between your assembly to optical maps, or your assembly to hybrid scaffolds for example. 

# Where to find maps
If you are using Bionano’s HybridScaffold pipeline from the IrysSolve package to generate super-scaffolds, the output directory hybrid_scaffolds/ stores all the maps that can be visualised. 

Here are some examples of alignments you can display:

Bionano maps against draft assembly     --->	   BNGcontigs_NGScontigs
Draft assembly against super-scaffolds  --->	   NGScontigs_HYBRID_SCAFFOLD
Bionano maps against super-scaffolds    --->	   BNGcontigs_HYBRID_SCAFFOLD

* XMAP file: this file contains the alignment information and finishes with an “.xmap” file extension.
* Reference CMAP file: this contains all the contigs for the “reference” dataset, (those which the queries have been aligned to). This is often noted by the “_r.cmap” file extension.
* Query CMAP file: this contains all the contigs for the “query” dataset, (those which are aligned to the reference). This is often noted by the “_q.cmap” file extension.

*<b>Note: this program is aimed to visualise the alignment during the hybrid scaffolding stage of analysis, not the de novo map assembly – loading maps from the de novo step of alignment may lead to a crowded and unclear display as these alignments often contains a large number of query contigs aligned to one reference</b>*

Once maps have been loaded, click Run and the software should populate the table of reference contigs and draw the reference graphs in Summary View so the data can begin to be explored.

----
# Summary View
Summary View provides a good overview for the user to navigate through the contigs of the reference dataset using the table on the left, to view the alignments and quality of the contigs on the right.

