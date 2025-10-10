package se.fk.github.jaxrsclientfactory;

public class WhateverVO
{
   private final String attribute1;

   public WhateverVO()
   {
      this.attribute1 = null;
   }

   public WhateverVO(String attribute1)
   {
      this.attribute1 = attribute1;
   }

   public String getAttribute1()
   {
      return attribute1;
   }

   @Override
   public String toString()
   {
      return "WhateverVO [attribute1=" + attribute1 + "]";
   }
}
