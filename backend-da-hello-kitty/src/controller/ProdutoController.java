package controler;

@RestController
@RequestMapping("api/produtos")

public class ProdutoController {
    private final ProdutoDao produtoDao;

    public ProdutoController(ProdutoDao produtoDao){
        this.produtoDao = produtoDao;
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoDao.listarTodos();
    }

    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id){
        return produtoDao.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody ProdutoRequest request){
        Produto produto = produtoDao.criar(request);
        return ResponseEntity.created(URI.create("/api/produtos/" + produto.getId())).body(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request){
        return produtoDao.atualizar();
    }

    @DeleteMapping("/{id}")
    public ReposeEntity<Void> excluir(@PathVariable Long id){
        produtoDao.excluir(id);
        return ResponseEntity.noContext().build();
    }
}
