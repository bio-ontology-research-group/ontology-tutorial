
#!/usr/bin/perl
#use strict;
#Define all Needed Variables
use List::MoreUtils qw(uniq);
my $path = `pwd`;
my $annotfile=$ARGV[0];
chomp ($path);
#my $annotfile="$path/annotationAxiom.lst";
my @array =();
open (FH,$annotfile);
while (my $line=<FH>)
{
	chomp ($line);
	if ($line =~ /(\S+)\s+(\S+)/)
	{
		my $class1 =$1;
		my $class2 =$2;
		push @array, $class1;
		push @array, $class2;
	}
}

open (OUTF, ">>", "annotclasses.lst");
foreach (@array)
{
	print (OUTF "$_\n");
}	
#print (OUTF "$_\n");
close (OUTF);

`touch allclasses.lst`;

`touch finalclasses.lst`;

`cat classes.lst annotclasses.lst > allclasses.lst`;

` uniq -u allclasses.lst > finalclasses.lst`;
