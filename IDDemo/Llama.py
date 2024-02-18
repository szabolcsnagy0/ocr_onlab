from langchain_core.callbacks import StreamingStdOutCallbackHandler
from langchain_core.language_models import LLM
from llama_cpp import Llama
from langchain.prompts import PromptTemplate
from langchain.callbacks.manager import CallbackManager
from langchain_community.llms import LlamaCpp

template = """Question: {question}

Answer: Let's work this out in a step by step way to be sure we have the right answer."""

# template = """Question: {question}
#
# Answer: Be precise and short."""

# prompt = PromptTemplate.from_template(template)
#
# callback_manager = CallbackManager([StreamingStdOutCallbackHandler()])
#
# llm = LlamaCpp(
#     model_path="D:/llama-2-7b-chat.Q2_K.gguf",
#     temperature=0.75,
#     max_tokens=2000,
#     top_p=1,
#     callback_manager=callback_manager,
#     verbose=True,  # Verbose is required to pass to the callback manager
# )
#
# prompt = """
# Question: Is Szabolcs Nagy a grammatically correct hungarian name? Answer only with yes or no!
# """
# llm.invoke(prompt)

llm = Llama(
    model_path="D:/llama-2-7b-chat.Q2_K.gguf"
)
output = llm(
      "Q: Answer the following question only with yes or no! Is Petre Kiss a grammatically correct hungarian name? A: ", # Prompt
      max_tokens=32, # Generate up to 32 tokens, set to None to generate up to the end of the context window
      stop=["Q:", "\n"], # Stop generating just before the model would generate a new question
      echo=True # Echo the prompt back in the output
) # Generate a completion, can also call create_completion


print(output['choices'][0]['text'])
