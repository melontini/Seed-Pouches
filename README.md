# <img src="https://user-images.githubusercontent.com/104443436/174792973-21d1973b-2513-462d-8d2c-cea9bb11ed6a.png" width="50"> Overly Complicated Seed-Pouch Mod

 Why is it "Pouches" if there's only 1 pouch?
 
 ## Items
 This mod adds just one item, but it's probably the best item in the game.
 
 Meet the Seed Pouch!
 
 <img src="https://user-images.githubusercontent.com/104443436/174796518-9a2e2bea-071b-4a3e-aa04-66b63e740b0a.png" width="300">
 
 ### So what can you do with the damn thing?
 
 Good Question! When you use the pouch it releases a projectile which, upon landing, drops a plethora of different crops.

![ThrowBag](https://user-images.githubusercontent.com/104443436/174799839-f14a1575-925d-4d1e-85dd-467eeb5ef29c.gif)
 
 But upon colliding with a player, the bag will immediately insert all of it's loot into that player's inventory, without dropping anything on the ground.
 
 ![BagThrowPlayer](https://user-images.githubusercontent.com/104443436/174800732-1ac1cbf1-bc36-4283-8efa-f697a6676f4f.gif)
 
 You can also just use the bag on a player to achieve the same effect without releasing the projectile.
 
 ### The Second Best Feature
 
 ![DropperBag](https://user-images.githubusercontent.com/104443436/174802849-fcfda792-0ae4-49c9-813c-f8e920a43de8.gif)
 
 ## New Gamerule
 
 This mod also adds a new GameRule
 
 autoPlantCropsWhenPossible (off by default)
 
 As you can probably guess, with this rule on, crops will try to plant themselves if the block beneath them is where they can be planted. If that makes sense.
 
 ![PlantsGrowing](https://user-images.githubusercontent.com/104443436/174808459-32ee719c-5c44-4c5f-b0b9-0721507a43db.gif)
 
 ## Custom Drops
 
 If you want the pouch to drop additional seeds, you can overwrite
 
 `seed_pouches:loot_tables/dropped_seeds.json`
 
 with KubeJS or a data pack to achieve this.
