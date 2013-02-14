package me.ayan4m1.plugins.breedmeister;
 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ 
/*    */ public class Config
/*    */ {
/*    */   public HashMap<String, Integer> values;
/*    */   private YamlConfiguration config;
/*    */ 
/*    */   public Config(BreedMeister instance)
/*    */   {
/* 13 */     this.values = new HashMap();
/*    */ 
/* 15 */     this.config = ((YamlConfiguration)instance.getConfig());
/*    */ 
/* 17 */     if (this.config.isInt("maxmobs")) { this.values.put("maxmobs", Integer.valueOf(this.config.getInt("maxmobs")));
/*    */     } else {
/* 19 */       this.config.set("maxmobs", Integer.valueOf(40));
/* 20 */       this.values.put("maxmobs", Integer.valueOf(40));
/*    */     }
/*    */ 
/* 17 */     if (this.config.isInt("maxdistance")) { this.values.put("maxdistance", Integer.valueOf(this.config.getInt("maxdistance")));
/*    */     } else {
/* 19 */       this.config.set("maxdistance", Integer.valueOf(5));
/* 20 */       this.values.put("maxdistance", Integer.valueOf(5));
/*    */     }

/* 17 */     if (this.config.isInt("breedelay")) { this.values.put("breedelay", Integer.valueOf(this.config.getInt("breedelay")));
/*    */     } else {
/* 19 */       this.config.set("breedelay", Integer.valueOf(5));
/* 20 */       this.values.put("breedelay", Integer.valueOf(5));
/*    */     }

/* 17 */     if (this.config.isInt("spawnradius")) { this.values.put("spawnradius", Integer.valueOf(this.config.getInt("spawnradius")));
/*    */     } else {
/* 19 */       this.config.set("spawnradius", Integer.valueOf(5));
/* 20 */       this.values.put("spawnradius", Integer.valueOf(5));
/*    */     }
/*    */ 
/* 52 */     instance.saveConfig();
/*    */   }
/*    */ }

/* Location:           C:\Users\Nicholas\Desktop\breedmeister.jar
 * Qualified Name:     net.gb.chrizc.breedmeister.Config
 * JD-Core Version:    0.6.2
 */