#!/usr/bin/perl
#use strict

#Variables Definition
my $metadata_file =$ARGV[0];
my %annotation_map=();

#1.Build annotation map
open (FILE1,"$metadata_file");
while (my $line1=<FILE1>)
{
	chomp ($line1);
	if ($line1=~/(\S+)\s+(\S+)\s+(.+)/)
	{
		my $class =$1;
		my $property =$2;
		my $literal=lc($3);
		$literal=~s/[.]+//g;
		$literal =~ s/[^a-zA-Z0-9,\s\t<>_:]//g;	
		if ($property =~/Synonym/ or $property =~/rdfs:label/)
		{	
			if ((length ($literal))>=3 and ($literal ne "as"))
			{					
				$annotation_map{$literal}=$class;
			}		
		}	
	}
}

close (FILE1);
for (keys %annotation_map)
{
	#print ("$_ $annotation_map{$_}\n");
}

#2.Annotate the metadata
open (FILE2, "$metadata_file");
while (my $line2 =<FILE2>)
{
	chomp ($line2);
	if ($line2=~/(\S+)\s+(\S+)\s+(.+)/)
	{
		my $class=$1;
		my $property=$2;
		my $literal=lc($3);
		$literal=~s/[.]+//g;
		#$literal=~ s/[^a-zA-Z0-9,\s\t<>_:]//g;		
		if ($property =~/IAO_0000115/)
		{	
			#3. Annotate Description Text			
			foreach $key (keys %annotation_map)
			{
				#print ("Literal: $literal\n");				
				my $text=$key;
				my $anno_class=" $annotation_map{$text} ";
				$literal=~s/\s+$text\s+/$anno_class/g;
				
			}
		}
	print ("$class $property $literal\n");	
	}
}

