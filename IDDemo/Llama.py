from langchain_core.callbacks import StreamingStdOutCallbackHandler
from langchain_core.language_models import LLM
from llama_cpp import Llama, LlamaGrammar
from langchain.prompts import PromptTemplate
from langchain.callbacks.manager import CallbackManager
from langchain_community.llms import LlamaCpp
import io

# # Langchain
#
# template = """
# You are a linguist who knows all existing hungarian first and last names.
#
# Question: Is {name} a grammatically correct hungarian first name? If not, correct it.
#
# Answer: """
#
# callback_manager = CallbackManager([StreamingStdOutCallbackHandler()])
#
# llm = LlamaCpp(
#     model_path="D:/llama-2-7b-chat.Q2_K.gguf",
#     temperature=0.1,
#     max_tokens=2000,
#     top_p=1,
#     stop=["\n"],
#     callback_manager=callback_manager,
#     verbose=True,  # Verbose is required to pass to the callback manager
#     grammar_path="../grammars/grammar.gbnf"
# )
#
# prompt = template.format(name="Szabokcs")
# # prompt = template.format(name="Nagy Szabolcs")
# llm.invoke(prompt)

# # llama_cpp
#
# llm = Llama(
#     model_path="D:/llama-2-7b-chat.Q2_K.gguf"
# )
# output = llm(
#       "Q: Is Szabolds a grammatically correct hungarian first name? If not, what is the correct form? A: ", # Prompt
#       max_tokens=50,
#       stop=["Q:", "\n"], # Stop generating just before the model would generate a new question
#       echo=False, # Echo the prompt back in the output
# ) # Generate a completion, can also call create_completion
#
# print(output['choices'][0]['text'])


# Trying out specific grammar

# file = io.open("../images/ocr_data_mixed.txt", mode="r", encoding="utf8")
# ocr_text = file.read()
# file.close()
#
# callback_manager = CallbackManager([StreamingStdOutCallbackHandler()])
#
# llm = LlamaCpp(
#     model_path="D:/llama-2-7b-chat.Q6_K.gguf",
#     temperature=0.75,
#     max_tokens=2000,
#     top_p=1,
#     callback_manager=callback_manager,
#     verbose=True,  # Verbose is required to pass to the callback manager
#     # grammar_path="../grammars/id_grammar.gbnf"
# )
#
# prompt = ("The following data is from an OCR scan of a person's identity. Try to process it knowing, it might contain "
#           "spelling errors or it might have missing data. Try correcting the errors you find and tell me "
#           "what you know about the person!")
# llm.invoke(prompt + "\n" + ocr_text)


# Trained Model

import torch
from transformers import T5Model, AutoTokenizer, AutoModelForCausalLM

MODEL_PATH = "../../top_names"
ACCESS_TOKEN = "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"

tokenizer = AutoTokenizer.from_pretrained(MODEL_PATH, token=ACCESS_TOKEN)
model = AutoModelForCausalLM.from_pretrained(
    MODEL_PATH,
    device_map="auto",
    torch_dtype='auto'
).eval()

input_context = """
###Human:
Correct this hungarian first name: Szabojcs

###Assistant:
"""

input_ids = tokenizer.encode(input_context, return_tensors="pt")
output = model.generate(input_ids, max_length=85, temperature=0.3, num_return_sequences=1)

generated_text = tokenizer.decode(output[0], skip_special_tokens=True)

print(generated_text)