import java.util.List;

/**
 * @author KarelPetranek
 *
 * Contains list of models for the given correct answer
 */
public class ModelsAndAnswer  {
	public Answer answer;
	public List<Model> models;
	
	/**
	 * Create answer-models pair
	 * @param answer Correct answer
	 * @param models Corresponding models
	 */
	public ModelsAndAnswer(Answer answer, List<Model> models)  {
		this.answer = answer;
		this.models = models;
	}

	@Override
	public String toString() {
		return "ModelsAndAnswer [answer=" + answer + ", models=" + models + "]";
	}
	
	
}