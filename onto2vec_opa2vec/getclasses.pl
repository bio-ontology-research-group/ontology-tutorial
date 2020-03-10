
#!/usr/bin/perl
#use strict;
#Define all Needed Variables
use List::MoreUtils qw(uniq);
my $path = `pwd`;
my $annotfile=$ARGV[0];
my $annot_classes=$ARGV[1];
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

open (OUTF, ">>", $annot_classes);
foreach (@array)
{
	print (OUTF "$_\n");
}
close (OUTF);


