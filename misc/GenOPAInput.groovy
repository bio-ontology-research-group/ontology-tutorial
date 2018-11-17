new File("MGI_GenePheno.rpt").splitEachLine("\t") { line ->
    def id = line[-2]
    def p = "<http://purl.obolibrary.org/"+line[-4].replaceAll(":","_")+">"
    println id+"\t"+p
}

new File("phenotype_annotation.tab").splitEachLine("\t") { line ->
    def id = line[0]+":"+line[1]
    def p = "<http://purl.obolibrary.org/obo/"+line[4].replaceAll(":", "_")+">"
    println id+"\t"+p
}
