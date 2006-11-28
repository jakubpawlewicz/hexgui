#!/usr/bin/perl

system("rm -rf Version.java");

open(SVN, "svn_version");
$svn = <SVN>;
close(SVN);
chomp($svn);

$date = localtime(time);

open(OUT, ">Version.java");
open(FILE, "Version.java.in");
foreach $line (<FILE>) {
    $line =~ s/\@DATE\@/$date/;
    $line =~ s/\@BUILD\@/$svn/;
    print OUT $line;
}
close FILE;
close OUT;




