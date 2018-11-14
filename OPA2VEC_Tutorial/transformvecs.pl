#!/usr/bin/perl
#use strict;
#Define all Needed Variables
use List::MoreUtils qw(uniq);
my $infile="p11";

open (FH,$infile);
while (my $line =<FH>)
{
	if ($line =~/^(\S+)\s+(.+)/)
	{
		my $enz_name=$2;;	
		print ("$enz_name\n");	
	}
}  


