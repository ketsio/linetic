Create your own Analyzer
=====================

Let's first have a look at the minimal code to create an `Analyzer` :

```java
package ch.linetic.analysis.analyzers;

import java.util.Iterator;
import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.MovementInterface;

class MyAnalyzer extends Analyzer {

	public final static float MIN_VALUE = /*1*/;
	public final static float MAX_VALUE = /*1*/;
	public final static boolean DO_ADJUST = /*2*/;

	public MyAnalyzer(int index) {
		super(index, MIN_VALUE, MAX_VALUE, DO_ADJUST);
	}

	@Override
	protected float compute(MovementInterface movement) {
		/*3*/
	}

	@Override
	public String name() {
		return "My Analyzer"; /*4*/
	}

	@Override
	public String getSlug() {
		return "my_analyzer"; /*5*/
	}

	@Override
	public boolean doTrigger(float finalValue) {
		return finalValue >= 0; /*6*/
	}
}

```

I suggest you copy/paste this code every time you want to create a new `Analyzer`.

**Now let's have a closer look to this code :** (see commented numbers)

 1. First thing you need to know is that there are two kinds of return value: The raw value and the final value. After the analysis, the `compute(MovementInterface movement)` method returns the raw value. The raw result is then rescaled to the final value by taking 4 parameters into account :  
  - `ch.linetic.analysis.AnalyzerInterface.RANGE_MIN` : The minimum final value common to all the analyzers
  - `ch.linetic.analysis.AnalyzerInterface.RANGE_MAX` : The maximum final value common to all the analyzers
  - `MIN_VALUE` : The minimum raw value that should be rescaled to `RANGE_MIN`
  - `MAX_VALUE` : The maximum raw value that should be rescaled to `RANGE_MAX`

 The raw value is thus rescaled proportionally so that `MIN_VALUE` becomes `RANGE_MIN` and `MAX_VALUE` becomes `RANGE_MAX`. *Note that `MAX_VALUE` can be smaller then `MIN_VALUE` if we want a reverse proportionality.*
 
 2. If and only if `DO_ADJUST` is true, we use the class `Statistics` to keep record of the data and use the confidence interval to adjust the final result. *Note that this option can be omitted, default value is true.*
 
 3. The `compute` method contains the behavior of the analyzer. It is the function that return the raw value. *Note that it makes more sens to first write that method and then adjust the value of `MIN_VALUE` and `MAX_VALUE` according to this raw value.*
 It takes a `MovementInterface` as an argument. Have a look at the [java documentation][Javadoc MovementInterface] to see what this interface can provide.

 4. This function simply returns the name that could be displayed when we display the `Analyzer` in the GUI

 5. This function returns the slug of the `Analyzer`. The slug is the identifier of the `Analyzer`, this is what will be use the recognize it.  **It is important that no two `Analyzer`s have the same slug.**

 6. The function `doTrigger` is the function that decides when we should notify the clients of the final result. It takes the final result as an argument and it returns true if we should trigger the clients, false otherwise. Just return true if you always want to notify the clients of the final result.


**Right, now save your Analyzer to `ch.linetic.analysis.analyzers` and add it to the `AnalyzerManager` like that :**

*in `ch.linetic.analysis.Analyzer` line 37*

```java 
	// ---------------//
	// ---Analyzers---//
	// ---------------//

	public final static AnalyzerInterface SPEED = new SpeedAnalyzer(0);
	public final static AnalyzerInterface SHAKINESS = new ShakinessAnalyzer(1);
	public final static AnalyzerInterface HAND_SHAKINESS = new HandShakinessAnalyzer(2);
	public final static AnalyzerInterface HANDS_PROXIMITY = new HandsProximityAnalyzer(3);
	public final static AnalyzerInterface REVERSE = new OrientationAnalyzer(4);
	public final static AnalyzerInterface CLAP = new ClapAnalyzer(5);
	public final static AnalyzerInterface MULTIPLICITY = new MultiplicityAnalyzer(6);

	// My Analyzers :
	public final static AnalyzerInterface MY_ANALYZER = new MyAnalyzer(7)

	/**
	 * The list of All analyzers used in the project
	 */
	public final static List<AnalyzerInterface> ALL = Arrays.asList(SPEED,
			SHAKINESS, HAND_SHAKINESS, HANDS_PROXIMITY, REVERSE, MULTIPLICITY, CLAP, MY_ANALYZER);
```
 - So First you create a static instance of your analyzer
 - And then you add it to the static list `ALL`

Now launch **Linetic** and enjoy your Analyzer !

 
[Javadoc MovementInterface]: /javadoc/ch/linetic/gesture/MovementInterface.html "Javadoc MovementInterface"