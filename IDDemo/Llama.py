from langchain_core.callbacks import StreamingStdOutCallbackHandler
from langchain_core.language_models import LLM
from llama_cpp import Llama, LlamaGrammar
from langchain.prompts import PromptTemplate
from langchain.callbacks.manager import CallbackManager
from langchain_community.llms import LlamaCpp
import io

# # Langchain
#
# with io.open("../names/test.txt", mode="r", encoding="utf8") as f:
#     words = f.read().splitlines()
#
# for word in words:
#     prompt = "Javítsd ki ezt a keresztnevet, tudva, hogy a névben 0 vagy több betű fel lett cserélve: {word}".format(word=word)
#     # prompt = """
#     # ###Human:
#     # Correct this hungarian first name: {word}
#     #
#     # ###Assistant:
#     # """.format(word=word)
#
#     callback_manager = CallbackManager([StreamingStdOutCallbackHandler()])
#
#     llm = LlamaCpp(
#         model_path="D:/PULI-LlumiX-32K-Q6_K.gguf",
#         temperature=0.7,
#         max_tokens=2000,
#         top_p=1,
#         stop=["\n"],
#         callback_manager=callback_manager,
#         verbose=True,  # Verbose is required to pass to the callback manager
#         # grammar_path="../grammars/grammar.gbnf"
#     )
#
#     llm.invoke(prompt)

# llama_cpp

llm = Llama(
    model_path="D:/top-names-13b-v1.5.gguf"
)

# Read the words from the input file
with io.open("../names/test.txt", mode="r", encoding="utf8") as f:
    words = f.read().splitlines()

for word in words:
    prompt = """
    ###Human:
    Correct this hungarian first name, knowing that some letters in the word might have been changed: {word}

    ###Assistant:
    """.format(word=word)
    # prompt = """
    # ###Human:
    # Correct this hungarian first name: {word}
    #
    # ###Assistant:
    # """.format(word=word)
    output = llm(
        prompt,  # Prompt
        max_tokens=200,
        stop=["Q:", "\n"],  # Stop generating just before the model would generate a new question
        echo=True,  # Echo the prompt back in the output
    )  # Generate a completion, can also call create_completion

    print(output['choices'][0]['text'])

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
