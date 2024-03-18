import csv
import io
import itertools

similar_letters = {
    'A': ['A', 'Á'],
    'Á': ['A', 'Á'],
    'B': ['B', 'D', 'P'],
    'C': ['C'],
    'D': ['B', 'D', 'P'],
    'E': ['E', 'É', 'F'],
    'É': ['E', 'É', 'F'],
    'F': ['F', 'E', 'É'],
    'G': ['G'],
    'H': ['H'],
    'I': ['Í', 'I', 'J'],
    'Í': ['I', 'Í', 'J'],
    'J': ['I', 'Í', 'J'],
    'K': ['K'],
    'L': ['L'],
    'M': ['M'],
    'N': ['N'],
    'O': ['Ó', 'Ö', 'Ő', 'O'],
    'Ó': ['O', 'Ö', 'Ő', 'Ó'],
    'Ö': ['O', 'Ó', 'Ő', 'Ö'],
    'Ő': ['O', 'Ó', 'Ö', 'Ő'],
    'P': ['B', 'D', 'P'],
    'Q': ['Q'],
    'R': ['R'],
    'S': ['S'],
    'T': ['T'],
    'U': ['Ú', 'Ü', 'Ű', 'U', 'V'],
    'Ú': ['U', 'Ü', 'Ű', 'Ú', 'V'],
    'Ü': ['U', 'Ú', 'Ű', 'Ü', 'V'],
    'Ű': ['U', 'Ú', 'Ü', 'Ű', 'V'],
    'V': ['V', 'Ú', 'Ü', 'Ű', 'U'],
    'W': ['W'],
    'X': ['X'],
    'Y': ['Y'],
    'Z': ['Z'],
    'a': ['a', 'á'],
    'á': ['a', 'á'],
    'b': ['b', 'q'],
    'c': ['c', 'e', 'o'],
    'd': ['d', 'q'],
    'e': ['e', 'é', 'c'],
    'é': ['e', 'é', 'c'],
    'f': ['f'],
    'g': ['g', 'q'],
    'h': ['h', 'n'],
    'i': ['í', 'i', 'l', 'j'],
    'í': ['i', 'í', 'l', 'j'],
    'j': ['i', 'í', 'j'],
    'k': ['k'],
    'l': ['i', 'í', 'l'],
    'm': ['m'],
    'n': ['n'],
    'o': ['ó', 'ö', 'ő', 'o'],
    'ó': ['o', 'ö', 'ő', 'ó'],
    'ö': ['o', 'ó', 'ő', 'ö'],
    'ő': ['o', 'ó', 'ö', 'ő'],
    'p': ['b', 'p'],
    'q': ['q', 'g'],
    'r': ['r'],
    's': ['s'],
    't': ['t'],
    'u': ['ú', 'ü', 'ű', 'u'],
    'ú': ['u', 'ü', 'ű', 'ú'],
    'ü': ['u', 'ú', 'ű', 'ü'],
    'ű': ['u', 'ú', 'ü', 'ű'],
    'v': ['v'],
    'w': ['w'],
    'x': ['x'],
    'y': ['y'],
    'z': ['z'],
}

# similar_letters = {
#     'A': ['A', 'Á'],
#     'Á': ['A', 'Á'],
#     'B': ['B', 'D', 'P'],
#     'C': ['C'],
#     'D': ['B', 'D', 'P'],
#     'E': ['E', 'É', 'F'],
#     'É': ['E', 'É', 'F'],
#     'F': ['F', 'E', 'É'],
#     'G': ['G'],
#     'H': ['H'],
#     'I': ['Í', 'I', 'J'],
#     'Í': ['I', 'Í', 'J'],
#     'J': ['I', 'Í', 'J'],
#     'K': ['K', 'X'],
#     'L': ['L'],
#     'M': ['M'],
#     'N': ['N'],
#     'O': ['Ó', 'Ö', 'Ő', 'O'],
#     'Ó': ['O', 'Ö', 'Ő', 'Ó'],
#     'Ö': ['O', 'Ó', 'Ő', 'Ö'],
#     'Ő': ['O', 'Ó', 'Ö', 'Ő'],
#     'P': ['B', 'D', 'P'],
#     'Q': ['Q'],
#     'R': ['R'],
#     'S': ['S'],
#     'T': ['T', 'F'],
#     'U': ['Ú', 'Ü', 'Ű', 'U', 'V'],
#     'Ú': ['U', 'Ü', 'Ű', 'Ú', 'V'],
#     'Ü': ['U', 'Ú', 'Ű', 'Ü', 'V'],
#     'Ű': ['U', 'Ú', 'Ü', 'Ű', 'V'],
#     'V': ['V', 'Ú', 'Ü', 'Ű', 'U'],
#     'W': ['W'],
#     'X': ['X', 'K'],
#     'Y': ['Y'],
#     'Z': ['Z'],
#     'a': ['a', 'á', 'o', 'ó'],
#     'á': ['a', 'á', 'o', 'ó'],
#     'b': ['b', 'd', 'p', 'q'],
#     'c': ['c', 'e', 'o'],
#     'd': ['b', 'd', 'p', 'q'],
#     'e': ['e', 'é', 'c'],
#     'é': ['e', 'é', 'c'],
#     'f': ['f', 't'],
#     'g': ['g', 'q'],
#     'h': ['h', 'n'],
#     'i': ['í', 'i', 'l', 'j'],
#     'í': ['i', 'í', 'l', 'j'],
#     'j': ['i', 'í', 'l', 'j'],
#     'k': ['k', 'x'],
#     'l': ['i', 'í', 'l', 'j'],
#     'm': ['m'],
#     'n': ['n'],
#     'o': ['ó', 'ö', 'ő', 'o', 'a', 'á'],
#     'ó': ['o', 'ö', 'ő', 'ó', 'a', 'á'],
#     'ö': ['o', 'ó', 'ő', 'ö', 'a', 'á'],
#     'ő': ['o', 'ó', 'ö', 'ő', 'a', 'á'],
#     'p': ['b', 'd', 'p', 'q', 'g'],
#     'q': ['b', 'd', 'p', 'q', 'g'],
#     'r': ['r'],
#     's': ['s'],
#     't': ['t'],
#     'u': ['ú', 'ü', 'ű', 'u'],
#     'ú': ['u', 'ü', 'ű', 'ú'],
#     'ü': ['u', 'ú', 'ű', 'ü'],
#     'ű': ['u', 'ú', 'ü', 'ű'],
#     'v': ['v'],
#     'w': ['w'],
#     'x': ['x', 'k'],
#     'y': ['y'],
#     'z': ['z'],
# }


MAX_REPLACEMENTS = 2
def replace_letters(word):
    # Create a list of all possible replacements for each letter in the word
    replacements = []
    nr_replacements = MAX_REPLACEMENTS
    max_includes = 10
    # if word in top_words:
    #     nr_replacements = MAX_REPLACEMENTS + 1
    #     max_includes = 5
    # else:
    #     nr_replacements = MAX_REPLACEMENTS
    #     max_includes = 3
    for i in range(len(word)):
        letter = word[i]
        # Get the list of similar-looking letters for this letter
        similar = similar_letters.get(letter, [])
        # Add the list of similar-looking letters for this letter to the replacements list
        replacements.append(similar)

    # Generate all possible combinations of replacements
    times_included = 0
    for replacement_combination in itertools.product(*replacements):
        # Create a new word by replacing each letter in the original word with the corresponding replacement
        new_word = ''.join(replacement_combination)
        # Count the number of replacements in the new word
        num_replacements = sum(1 for i in range(len(word)) if word[i] != new_word[i])
        # If the number of replacements is less than or equal to the maximum number of replacements allowed, yield the new word
        if num_replacements <= nr_replacements and times_included < max_includes:
            times_included = times_included+1
            yield new_word


# Read the words from the input file
with io.open("../names/top50_names.txt", mode="r", encoding="utf8") as f:
    top_words = f.read().splitlines()

# Read the words from the input file
with io.open("../names/top50_names.txt", mode="r", encoding="utf8") as f:
    words = f.read().splitlines()

# Create a CSV writer for the output file
with io.open("../train.csv", mode='w', newline='', encoding='utf8') as f:
    writer = csv.writer(f)

    # Write the header row
    writer.writerow(['scrambled', 'original', 'text'])

    i = 0

    # For each word, generate all possible replacements and write them to the output file
    for word in words:
        # if i % 1000 == 0:
        #     print(i)
        i = i+1
        for new_word in replace_letters(word):
            # Create the text with placeholders for the scrambled and original words
            text = f'###Human:\nCorrect this hungarian first name, knowing that some letters in the word might have been changed: {new_word}\n\n###Assistant:\n{word}'
            # text = f'###Human:\nCorrect this hungarian first name: {new_word}\n\n###Assistant:\n{word}'
            # Write the row to the output file
            writer.writerow([new_word, word, text])
