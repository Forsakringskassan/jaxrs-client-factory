package se.fk.github.jaxrsclientfactory;

public final class JaxrsClientOptionsBuilders
{
   private JaxrsClientOptionsBuilders()
   {
   }

   public static <T> JaxrsClientOptionsBuilder<T> createClient(String baseUrl, Class<T> clazz)
   {
      return new JaxrsClientOptionsBuilder<T>().setBaseUrl(baseUrl).setClazz(clazz);
   }
}
