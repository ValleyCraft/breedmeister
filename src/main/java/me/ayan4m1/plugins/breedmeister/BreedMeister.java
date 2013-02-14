/*     */ package me.ayan4m1.plugins.breedmeister;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;

          import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Dispenser;
import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Animals;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockDispenseEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ public class BreedMeister extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*  23 */   private HashMap<Integer, Long> breedTimes = new HashMap();
/*     */ 
/*  26 */   //private Integer maxDistance = Integer.valueOf(5);
               

//private Integer maxDistance = (BreedMeister.this.getConfig().getInt("maxdistance"));
//private Integer breedDelay = (BreedMeister.this.getConfig().getInt("breedelay"));
//private Integer spawnRadius = (BreedMeister.this.getConfig().getInt("spawnradius"));

/*  29 */   //private Integer breedDelay = Integer.valueOf(5);
            

            //private Integer spawnRadius = Integer.valueOf(5);


/*     */   public void onEnable() {
/*  35 */     getServer().getPluginManager().registerEvents(this, this);
              Config c = new Config(this);
/*     */   }
/*     */ 
/*     */   public void onDisable() {
/*  39 */     this.breedTimes.clear();
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onEntityDeath(EntityDeathEvent event)
/*     */   {
/*  45 */     Integer entityId = Integer.valueOf(event.getEntity().getEntityId());
/*  46 */     if (this.breedTimes.containsKey(entityId))
/*  47 */       this.breedTimes.remove(entityId);
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onBlockDispense(BlockDispenseEvent event)
/*     */   {
	          
/*  53 */     Block block = event.getBlock();
/*     */ 
/*  56 */     if ((block.getType() != Material.DISPENSER) || (event.getItem().getType() != Material.WHEAT)) {
/*  57 */       return;
/*     */     }
/*     */ 
/*  60 */     List entities = block.getWorld().getEntities();

/*  63 */     Animals animalOne = findValidAnimal(entities, block.getLocation(), null);
/*  64 */     if (animalOne == null) {
/*  65 */       event.setCancelled(true);
/*  66 */       return;
/*     */     }
/*     */ 
/*  69 */     Animals animalTwo = findValidAnimal(entities, block.getLocation(), animalOne);
/*  70 */     if (animalTwo == null) {
/*  71 */       event.setCancelled(true);
/*  72 */       return;
/*     */     }
 
             Integer total = TotalAnimals(entities, block.getLocation());
             String oof = Integer.toString(total);
               getLogger().info("DEBUG: Total Animals: " + oof);
             
/*     */ 
/*  76 */     Location newLoc = getNearestFreeBlock(animalTwo.getLocation());
/*  77 */     if (newLoc == null) {
/*  78 */       getLogger().warning("Tried to spawn baby, but couldn't find a free block!");
/*  79 */       event.setCancelled(true);
/*  80 */       return;
/*     */     }
              int maxmobs = (BreedMeister.this.getConfig().getInt("maxmobs"));          
              String wee = Integer.toString(maxmobs);
              if (total > maxmobs) {
            	  getLogger().warning("Animals are greater than " + wee + " in this Chunk stopping.");
            	  event.setCancelled(true);
            	  return;
              }
/*  84 */     Animals newAnimal = (Animals)block.getWorld().spawnCreature(newLoc, animalTwo.getType());
/*  85 */     newAnimal.setBaby();
/*     */ 
/*  88 */     //Long nextBreedTime = Long.valueOf(new Date().getTime() / 1000L + this.breedDelay.intValue() * 60);
                Long nextBreedTime = Long.valueOf(new Date().getTime() / 1000L + (BreedMeister.this.getConfig().getInt("breedelay")) * 60);
/*  89 */     this.breedTimes.put(Integer.valueOf(animalOne.getEntityId()), nextBreedTime);
/*  90 */     this.breedTimes.put(Integer.valueOf(animalTwo.getEntityId()), nextBreedTime);
/*     */ 
/*  93 */     Inventory dispInv = ((Dispenser)event.getBlock().getState()).getInventory();
              int wheat2 = dispInv.getSize();
/*  94 */     ItemStack wheat = dispInv.getItem(dispInv.first(Material.WHEAT));
              int yow = wheat.getAmount();
              String okok = Integer.toString(yow);
              getLogger().info("DEBUG: Wheat Left in stack: " + okok + "Also: " + wheat2);
/*  95 */     if (wheat.getAmount() > 1) {
/*  96 */       wheat.setAmount(wheat.getAmount() - 1);
                getLogger().info("DEBUG: Decreased wheat");
/*     */     }
                else {
                //wheat.setAmount(-1);
/*  98 */       dispInv.remove(wheat);
                getLogger().info("DEBUG: Removed wheat");
/*     */     }
/* 100 */     event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   private Location getNearestFreeBlock(Location animalLoc)
/*     */   {
/* 109 */     if (animalLoc.getBlock().isEmpty()) {
/* 110 */       return animalLoc;
/*     */     }
/*     */ 
/* 114 */     //for (int x = 0; x <= this.spawnRadius.intValue() * 2; x++) {
/* 115 */       for (int x = 0; x <= (BreedMeister.this.getConfig().getInt("spawnradius")) * 2; x++ ) {
                  for (int y = 0; y <= (BreedMeister.this.getConfig().getInt("spawnradius")) * 2; y++) {
/* 116 */         for (int z = 0; z <= (BreedMeister.this.getConfig().getInt("spawnradius")) * 2; z++) {
/* 117 */           Location testLoc = animalLoc.add(x % (BreedMeister.this.getConfig().getInt("spawnradius")) * (x < (BreedMeister.this.getConfig().getInt("spawnradius")) ? -1 : 0), y % (BreedMeister.this.getConfig().getInt("spawnradius")) * (y < (BreedMeister.this.getConfig().getInt("spawnradius")) ? -1 : 0), z % (BreedMeister.this.getConfig().getInt("spawnradius")) * (z < (BreedMeister.this.getConfig().getInt("spawnradius")) ? -1 : 0));
/*     */ 
/* 121 */           if (testLoc.getBlock().isEmpty()) {
/* 122 */             return testLoc;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   private Animals findValidAnimal(List<Entity> entities, Location dispenserLoc, Animals exceptThis)
/*     */   {
/* 139 */     for (Entity e : entities)
/*     */     {
/* 141 */       if (((e instanceof Animals)) && (((Animals)e).canBreed()) && (!e.isDead()) && 
/* 146 */         //(dispenserLoc.distance(e.getLocation()) <= this.maxDistance.intValue()))
		            (dispenserLoc.distance(e.getLocation()) <= (BreedMeister.this.getConfig().getInt("maxdistance"))))
/*     */       {
/* 151 */         if (this.breedTimes.containsKey(Integer.valueOf(e.getEntityId()))) {
/* 152 */           if (((Long)this.breedTimes.get(Integer.valueOf(e.getEntityId()))).longValue() <= new Date().getTime() / 1000L)
/*     */           {
/* 155 */             this.breedTimes.remove(Integer.valueOf(e.getEntityId()));
/*     */           }
/*     */ 
/*     */         }
/* 160 */         else if ((exceptThis == null) || (!e.getType().equals(exceptThis.getType())) || (exceptThis.getEntityId() != e.getEntityId()))
/*     */         {
/* 166 */           return (Animals)e;
/*     */         }
/*     */       }
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */

public Integer TotalAnimals(List<Entity> entities, Location dispenserLoc)
{
	int entcount = 0;
	/*
	 * Checks and applies entity cap using individual cap limits
	 */
	for (Entity entt : entities)
	{
		if (((entt instanceof Animals)) && (!entt.isDead())  && 
        (dispenserLoc.distance(entt.getLocation()) <= (BreedMeister.this.getConfig().getInt("maxdistance"))))
		{

			entcount ++;
	}
	}
	return entcount;
}
}

/* Location:           C:\Users\Nicholas\Desktop\bukkit\plugins\BreedMeister-0.2.jar
 * Qualified Name:     me.ayan4m1.plugins.breedmeister.BreedMeister
 * JD-Core Version:    0.6.2
 */