#!/usr/bin/perl
#use strict;
#Define all Needed Variables
use List::MoreUtils qw(uniq);
my $path = `pwd`;
my $annotfile=$ARGV[0];
chomp ($path);
#my $annotfile="$path/annotationAxiom.lst";
my $ancestfile= $ARGV[1];
my $ontoclassesfile= $ARGV[2];
my $addoutfile= $ARGV[3];
my @temparray=();
open (FILE, '>>', "$addoutfile");
open (FH,"$annotfile");
#Make class to superclass map
my %supermap=();
open (FILEANC, $ancestfile);
while (my $lineanc=<FILEANC>)
{
	if (($lineanc=~/(\S+)\s+SubClassOf\s+(.*)/) || ($lineanc=~/(\S+)\s+EquivalentTo\s+(.*)/))
	{
		my $child=$1;
		my $ances=$2;
		$supermap{$child}.=",$ances";
	}
}


#Add inferred axioms
while (my $lineassoc=<FH>)
{
	if ($lineassoc=~/(\S+)\s+(\S+)/)
	{
		my $entity=$1;
		my $classo=$2;
		my @supers=split(/,/,$supermap{$classo});
		foreach (@supers)
		{
			my $supclass= $_;			
			if (!($supclass eq ""))
			{
				print FILE "$entity $supclass\n";		
			}
		}	
	}	
}
