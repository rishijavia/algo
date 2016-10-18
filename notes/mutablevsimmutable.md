Immutability and Instances

To demonstrate this behaviour, we'll use java.lang.String as the immutable class and java.awt.Point as the mutable class.

```
  Point myPoint = new Point( 0, 0 );
	System.out.println( myPoint );
	myPoint.setLocation( 1.0, 0.0 );
	System.out.println( myPoint );

	String myString = new String( "old String" );
	System.out.println( myString );
	myString.replaceAll( "old", "new" );
	System.out.println( myString );
```

In case you can't see what the output is, here it is:
```
	java.awt.Point[0.0, 0.0]
	java.awt.Point[1.0, 0.0]
	old String
	old String
```

We are only looking at a single instance of each object, but we can see that the contents of myPoint has changed, but the contents of myString did not. To show what happens when we try to change the value of myString, we'll extend the previous example.
```
	String myString = new String( "old String" );
	System.out.println( myString );
	myString = new String( "new String" );
	System.out.println( myString );
```

The output from this is:
```
	old String
	new String
```

Now we find that the value displayed by the myString variable has changed. We have defined immutable objects as being unable to change in value, so what is happening? Let's extend the example again to watch the myString variable closer.
```
	String myString = new String( "old String" );
	String myCache = myString;
	System.out.println( "equal: " + myString.equals( myCache ) );
	System.out.println( "same:  " + ( myString == myCache ) );

	myString = "not " + myString;
	System.out.println( "equal: " + myString.equals( myCache ) );
	System.out.println( "same:  " + ( myString == myCache ) );
```

The result from executing this is:

```
	equal: true
	same:  true
	equal: false
	same:  false
```

What this shows is that variable myString is referencing a new instance of the String class. The contents of the object didn't change; we discarded the instance and changed our reference to a new one with new contents.

**Variable Values and Instance Contents**

If you look at the example above, you can see the point I'm trying to sneak through. You can always change the value of a variable by getting your variable to reference a new object. Sometimes you can change the value of a variable by keeping a reference to the same instance, but change the contents of the instance.
After you have eliminated those possibilities, you have a variable that retains its reference to an object, but the contents of this object cannot change. Doesn't sound like a very interesting idea, and it sounds a bit too simple to be useful.

It turns out that Immutable Objects, that is objects that you cannot change the contents after they have been set, are a very handy tool when used in the right place. They can promote thread safety in your code, you can share them around without being afraid that they will change without your knowledge, they are great for caching and constants. But we're not going to cover any of that yet; we are going to concentrate on building immutable objects.

**Protect mutable fields**

The last requirement which many people fall victim too, is to build your immutable class from primitive types or immutable fields, otherwise you have to protect mutable fields from manipulation.
To highlight this problem, we'll use the example of a supposedly immutable class representing a person. Our class has a first and last name, as well as a date of birth.
```java
	import java.util.Date;
	public final class BrokenPerson
	{
		private String firstName;
		private String lastName;
		private Date dob;

		public BrokenPerson( String firstName,
		  String lastName, Date dob)
		{
			this.firstName = firstName;
			this.lastName = lastName;
			this.dob = dob;
		}

		public String getFirstName()
		{
			return this.firstName;
		}
		public String getLastName()
		{
			return this.lastName;
		}
		public Date getDOB()
		{
			return this.dob;
		}
	}
```

This all looks fine, until someone uses it like this:

```
	Date myDate = new Date();
	BrokenPerson myPerson =
	  new BrokenPerson( "David", "O'Meara", myDate );
	System.out.println( myPerson.getDOB() );
	myDate.setMonth( myDate.getMonth() + 1 );
	System.out.println( myPerson.getDOB() );
```

Depending on the dates entered, the output could be something like this:

```
	Mon Mar 24 21:34:16 GMT+08:00 2003
	Thu Apr 24 21:34:16 GMT+08:00 2003
```

The Date object is mutable, and the myPerson variable is referencing the same instance of the Date object as the myDate variable. When myDate changes the instance it is referencing, the myPerson instance changes too. It isn't immutable!
We can defend against this by taking a copy of the of the Date instance when it is passed in rather than trusting the reference to the instance we are given.
```

	import java.util.Date;
	public final class BetterPerson
	{
		private String firstName;
		private String lastName;
		private Date dob;

		public BetterPerson( String firstName,
		  String lastName, Date dob)
		{
			this.firstName = firstName;
			this.lastName = lastName;
			this.dob = new Date( dob.getTime() );
		}
		//etc...
```

Now we're close, but we're still not quite there. Our class is still open to abuse.

```
	BetterPerson myPerson =
	  new BetterPerson( "David", "O'Meara", new Date() );
	System.out.println( myPerson.getDOB() );
	Date myDate = myPerson.getDOB();
	myDate.setMonth( myDate.getMonth() + 1 );
	System.out.println( myPerson.getDOB() );
```

We see here that taking a copy on the way in wasn't enough; we also need to prevent anyone from getting a reference to our mutable Date field when we pass it out.

```
	public Date getDOB()
	{
		return new Date( this.dob.getTime() );
	}
```

*Make deep copies of mutable data*

The only point to add is that when you copy the instance on the way in and the way out, you need to make a deep copy. Otherwise you run the risk of leaving some mutable data in your immutable class!
If you are confused about the need to provide a deep copy, keep in mind that a single piece of shared mutable data, no matter how deep it is buried inside an object, makes your class mutable. When you create a copy of an object to defend against the value changing, you need to make sure your copy doesn't include this shared mutable class. You need to copy any mutable objects all the way down to the last field, and copy any nested fields until you have a completely new copy of your own. It's the only way to be safe!

Our Template for Immutable Classes

Now we have a template for creating immutable objects.
- Make all fields private
- Don't provide mutators
- Ensure that methods can't be overridden by either making the class final (Strong Immutability) or making your methods final (Weak Immutability)
- If a field isn't primitive or immutable, make a deep clone on the way in and the way out.
