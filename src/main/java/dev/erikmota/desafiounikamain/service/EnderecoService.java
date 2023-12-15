package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private MonitoradorRepository monitoradorRepository;

    public void cadastrar(Endereco e){
        if (!repository.existsByCep(e.getCep())) {
            repository.save(e);
        }
        else
            throw new ValidacaoException("Este cep já está cadastrado");
    }

    public void editar(Long id, Endereco e){
        Endereco novoEndereco = repository.getReferenceById(id);
        novoEndereco.editar(e);
    }

    public List<Endereco> listar(){
        return repository.findAll();
    }

    public void excluir(Long id){
        Endereco e = repository.getReferenceById(id);
        if (repository.existsByCep(e.getCep()))
            repository.delete(e);
        else
            throw new ValidacaoException("Este cep não está cadastrado");
    }

    public List<Endereco> listarPorMonitorador(Long id) {
        return repository.findByMonitoradorId(id);
    }
}
