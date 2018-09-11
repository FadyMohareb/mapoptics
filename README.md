# MapOptics
MapOptics is a lightweight cross-platform tool that enables the user to visualise and interact with the alignment of Bionano optical mapping data and can be used for in depth exploration of hybrid scaffolding alignments.

# How to install MapOptics
#### 1. Through the Java JAR executable:
The easiest way to get started is to download the stable release here. Uncompres the zipped file and run the JAR executable directly.

Alternatively, you can clone the git repository:

```
git clone https://github.com/FadyMohareb/mapoptics
cd mapoptics/dist/
java -jar MapOptics.jar
```


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
![alt text](https://github.com/FadyMohareb/mapoptics/blob/master/UserGuide/Fig1.png "Summary View")

(A)	The table of reference contigs should be populated when the maps are loaded. This includes information on the reference contig length, number of labels, label density (/100kb), number of query contigs aligned to the reference and number of query contigs which overlap in regions of their alignment. The table can be sorted in order of any of these fields. When a row of the table is selected, the information of that reference is displayed.

(B)	The names of the loaded datasets can be altered in the “Reference Dataset” and “Query Dataset” fields. As there are many different combinations of maps that can be loaded, this allows the user to keep track of what datasets they are displaying.

(C)	This graph shows the distribution of reference contig lengths, highlighting the chosen contig in the distribution.

(D)	This graph shows the label density distribution of the reference contigs, highlighting the chosen contig in the distribution with its label density value. Ideally label density should fall between 10 and 20 labels per 100kb.

(E) This panel displays a simple diagram of all the query contigs that are aligned to the reference contig, with the orientation of their alignment. This view can be changed and updated from Reference View.

If a user wishes to explore the alignments of a reference contig in more detail, they can do so in Reference View.

# Reference View
Reference View gives the user more of an interactive view of the contig alignment with more information on aspects of their quality.
 ![alt text]( https://github.com/FadyMohareb/mapoptics/blob/master/UserGuide/Fig2.png "Reference View")
 
 
(A)	The display generated for Reference View is similar to that of Summary View but is more interactive. Here, the user can drag the contigs and screen so as to centre the view as required.

(B)	The query contigs that align to this reference should populate the table below the display. This shows information on their ID, length, orientation, confidence in the alignment, the CIGAR of the structural variation in the alignment, the number of labels and the number of labels that match within the alignment. Here, contigs can be selected and highlighted in the view or explored in more detail in Query View.

(C)	This is the “Display tools” portion of the tool bar. This is for customising the display. The user can zoom in on the view and reCentre to re-size the display to show all contigs. The Label style can be recoloured to display the labels that match, the coverage values of the labels or the chimeric quality of the labels. Confidence view can be selected, this changes the alignment lines to match the confidence in the alignment (solid line = high confidence, dashed line = medium confidence, dotted line = low confidence). Overlap view can be selected to display the regions in which query contigs overlap in their alignment, highlighted in yellow.

(D) This is the “Contig tools” portion of the tool bar. This is for manipulating the contigs in the display and saving their positioning. They can be reoriented in the display, position the contig to match the left or rightmost part of the alignment, can be deleted from the display and the positioning of the contigs can be saved to the Summary View for ease of navigation. The changes can be reset to default or last saved if the user is unhappy with changes made.

If the user wishes to explore a single alignment between one reference and one query contig, a query contig can be selected (from the display or table) and then explored in Query View.

# Query View
Query View shows the most in detail view of one alignment. Here the user can see a simplified view of one alignment and navigate through all the labels on the query contig.

![alt text](https://github.com/FadyMohareb/mapoptics/blob/master/UserGuide/Fig3.png "Query View")

(A)	The display generated for Query View is not as interactive as Reference View. By default, the query contig is oriented positively and positioned to match the leftmost alignment. The display settings match those set in Reference View (e.g. label style).

(B)	This table allows the user to navigate and highlight the labels on the query contig. This table contains information of the label’s position, coverage, occurrence, chimeric quality and standard deviation.

(C)	This additional table shows if the query contig has been aligned to other references in addition to the one displayed. These can be navigated through and all the equivalent views will be updated to match this reference (including Summary View and Reference View).

(D)	In this view, the user can search for any reference or query to be displayed (a message will be displayed of there is no alignment found between the two). There is also the option to zoom into a region of choice in either the reference or query contig. This can be of use when using a small screen, working with very long contigs or when FASTA file information is loaded and you would like to explore the position of the gaps in more detail.

----

# Other Functionalities
There are also some additional functionalities to the application to further enhance the functionality of MapOptics within your hybrid scaffolding research. 

## Available quick-tools
Some quick tools are available under Tools in the top toolbar.

|Function|Action|
| ------------- |:-------------:|
| Tools > Orientate all contigs     | Orientates the display of all contigs to be positive. |
| Tools > Save view of all contigs      | Saves any changes performed in Reference View to Summary View.      |
| Tools >  Swap query and reference | Swaps the two datasets so the query dataset becomes the reference dataset and the reference dataset becomes the query dataset.      |

# Loading a FASTA file
To explore the alignment of the maps in more detail, a FASTA file can be loaded to display the gaps in the sequence in Query View- this can help you recognise if what appear to be misalignments are actually caused by regions of unknown sequence.


FASTA and Key files can be loaded under File > Load FASTA and Key files. Here you must upload two files:
(1)	The FASTA file of the original assembly that you’re hybrid scaffolding.
(2)	The key file (with the extension _key.txt). This contains all the names of the FASTA contigs and the IDs that were given to them during hybrid scaffolding.

## Where to find the key file
*If you are using Bionano’s HybridScaffold pipeline from the IrysSolve package to generate super-scaffolds, the output directory fa2cmap/ stores all the files outputted when the FASTA is converted into a CMAP format. The key file can be found within this directory.

Both must be loaded for any gap information to be displayed.
Finally, the user must select which dataset the FASTA file is in regards to, the “reference” or “query” to ensure it is visualised properly. If this is incorrectly selected, the program won’t necessarily give an error, but the gaps will be visualised incorrectly leading to incorrect conclusions in an analysis. 



