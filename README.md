1. Using color palettes

2. Using FAB
	app gradle:
	dependancies
	implementation("com.google.android.material:material:1.12.0")

3. Creating dimesions and cliparts

4. Activity properties like theme, orientation and all defined in manifest

5. Bias: Bias, in terms of ConstraintLayout, means "if there is extra room, slide the widget in this direction along the axis". The default bias is 0.5, meaning that the widget is centered in the available space. For the horizontal axis, 0.0 means "slide the widget all the way towards the start side" and 1.0 means "slide the widget all the way towards the end side" (See addHappyPlace.xml)

6. EditText:Focusable and focusable on touch mode
Focusable false prevents us from typing in the date: We want to select from calendar (here date field)

7. Use 3rd party library

8. Use Dexter for getting permissions easily

9. Get Image for camera:
	Request Permission
	StartActivityForResult
	onActivityResult

10. Get Image from gallery:
	Same as above

11. Storing image to app scope storage

12. Database using SQLite:
	Create Database Handler
	Create AddtoDatabase in Handler(addHappyPlace)
	Call AddToDatabase in apt place
	Create Retrieve_all in Handler
	Call Retrieve_all in Activity

13. Starting activity for result

14. Passing and Checking Request codes

15. OnActivityResult and how to differentiate using Request_code

16. Checking ResultCode and requestCode

17. How to make recycler view items clickable</br>
	I. Create an interface inside adapter</br></br>
		interface onClickInterface{</br>
			//we need position to identify</br>
       			 //model to populate the detail activity</br>
        		fun onClick(position: Int, model: HappyPlaceModel)</br>
    		}</br></br>
	
	II. Create global variable inside class in adapter:</br></br>
		private var onClickListener: onClickInterface?=null</br></br>


	III. Function binding the onClickListener in Adapter</br></br>
		fun setOnClickListener(onClickListener: onClickInterface){</br>
       			 this.onClickListener=onClickListener</br>
    		}</br></br>

	IV. Inside the activity where the recycler view is existing:</br></br>
		First define the adapter. then:</br>
		
		adapter.setOnClickListener(object: HappyPlacesAdapter.onClickInterface{
            		override fun onClick(position: Int, model: HappyPlaceModel) {
                		val intent=Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                		startActivity(intent)
            		}
        	})


	V. In Adapter: Assign the onClickListener to each element</br></br>
		In OnBindViewHolder:</br>

		holder.itemView.setOnClickListener{
			//whatever you want to do when clicked
			if(onClickListener!=null){
               			 onClickListener!!.onClick(position,model)
            		}
		}


	Working: In bindViewHolder(V) every view is assigned a onClickListener. When we click it, it calls the onClick function (III). The adapter is 	informed to execute this function. But the adapter has overridden it (IV). So it calls the overridden version which opens the new activity</br></br></br>




18. How to send information from one Activity to other (info can be anything: string, int, objects of any class)
	      ```intent.putExtra("indentifier", int/bool/long etc)``` </br>
      	```startActivity(intent)```


19. If we want to pass an object of a custom class in intent:
	Make it serializable:
		In the definition of the custom class (here data class HappyPlaceMode):</br>
			data class HappyPlaceModel (//declarations):Serializable



20. How to retrieve the proper data:</br>
	In the activity (here HappyPlaceDetail) where we actually require the information:</br></br>
	
	      if(intent.hasExtra("required_identifier")){
		      //do whatever
		      happyPlaceDetailModel=intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel //we typecast it to HappyPlaceModel
	    }



21. Serializable vs ParcelAble -> Parcelble is faster

</br>
22. Creating Swipe to edit:</br>
	a.Create a utils directory	</br>
	b.Add the swipeToEdit.kt from medium</br>
	c.In the activity where the recycler view is there, create an editSwipeHandler object after creating the RV adapter (Here inside MainAct -> inside setupHappyPlaceRV )</br>
	d.Create a function inside the adapter class about notify. (Here inside HappyPlaceAdapter, notifyEditItem)</br>
	e.In the function created in step (c), Override the onSwiped function and call the notifyEditItem created in (d)</br>
	f.Do whatever you want to do after swiping (here we call an intent to addActivity and update details if the intent has extras)</br>
	g.Inside the activity having the RV, call the function created in (c)</br>
		Here:</br> 
    val editItemTouchHelper=ItemTouchHelper(editSwipeHandler)</br>
    editItemTouchHelper.attachToRecyclerView(binding?.rvHappyPlaces)</br>

</br></br>
23. Previous code was creating a new entry instead of updating the existing one: </br>
	a.Create update function inside the database handler</br>
	b.Check intent to see if edit or add has been called</br>
	c.Call relevant functions accordingly</br></br>

24. Swipe to delete (code is almost same, swiping direction different. So inside swipe directions also reversed)
	</br></br>

