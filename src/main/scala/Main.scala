import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*
import collection.mutable.Set
/*
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 */
object Main {


  @main def main = {
    //2 SETS
    Assign(Variable("testSet"), Insert(1,2,3,4,5)).eval()
    Assign(Variable("testSet2"), Insert(6,7,8,9,10)).eval()
    
    //Take the union of the two sets
    Assign(Variable("result"), Union(Variable("testSet"), Variable("testSet2"))).eval()
    
    //print the value of the new union set
    println(Variable("result").eval().getValue())
    
  }

}
