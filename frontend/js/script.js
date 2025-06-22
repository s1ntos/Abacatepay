function comprar(produto, valor) {
  const cliente = {
    name: "Bruno Cliente",
    cellphone: "(11) 66666-6666",
    email: "bruno@cliente.com",
    taxId: "247.934.869-00".replace(/\D/g, '') // Remove qualquer caractere não numérico
  };

  const valorNumerico = parseFloat(valor); // garante que é número, não string

  console.log("Enviando:", { produto, valor: valorNumerico, cliente });

  fetch('http://localhost:8080/api/pagar', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ produto, valor: valorNumerico, cliente })
  })
  .then(res => res.json())
  .then(dados => {
    console.log("Checkout criado:", dados);
    const urlCheckout = dados?.data?.url;
    if (urlCheckout) {
      window.location.href = urlCheckout;
    } else {
      alert("Erro ao obter URL do pagamento.");
    }
  })
  .catch(err => {
    console.error("Erro:", err);
    alert("Erro ao conectar com a API.");
  });
}
