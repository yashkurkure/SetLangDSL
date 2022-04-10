import SetLangDSL.*

import scala.collection.mutable

object myMain {


  class A{
    val set  = mutable.Set.empty[String]

    def this(a: A)={
      this()
      a.set.foreach(s=>this.set.add(s))
    }

    def insert(s: String): Unit = {
      set.add(s)
    }

    def deepCopy():A = {
      new A(this)
    }
  }

  def somefunc(v: Any): Unit ={
    v
  }



  @main
  def main(): Unit = {

    somefunc(throw RuntimeException("haha got you"))


  }

}
