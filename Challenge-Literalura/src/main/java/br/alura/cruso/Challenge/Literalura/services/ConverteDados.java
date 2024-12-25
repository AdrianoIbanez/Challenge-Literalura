package br.alura.cruso.Challenge.Literalura.services;

public class ConverteDados implements IConverteDados {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode toObject(String json) throws JsonProcessinException {
        return mapper.readTree(json);
    }

}