package com.example.lab7


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab7.adapters.ClientAdapter
import com.example.lab7.data.Client
import com.example.lab7.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var _listener: OnDataPassListener? = null
    private lateinit var _clients: ArrayList<Client>
    private lateinit var filteredClients: ArrayList<Client>

    interface OnDataPassListener {
        fun openDetailFragment(client: Client)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataPassListener) {
            _listener = context
        } else {
            throw RuntimeException("$context must implement OnDataPassListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _clients = arguments?.getParcelableArrayList<Client>(ARG_CLIENTS) ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        filteredClients = ArrayList(_clients)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ClientAdapter(filteredClients) { client ->
            _listener?.openDetailFragment(client)
        }
        recyclerView.adapter = adapter

        setupFilterSpinners(adapter)

        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        updateEmptyViewVisibility()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        _listener = null
    }

    companion object {
        private const val ARG_CLIENTS = "clients"

        fun newInstance(stringList: ArrayList<Client>): ListFragment {
            val fragment = ListFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_CLIENTS, stringList)
            fragment.arguments = args
            return fragment
        }
    }

    private fun setupFilterSpinners(adapter: ClientAdapter) {
        val fieldOptions = arrayOf("Кількість замовлень", "Сума витрат")
        val orderOptions = arrayOf("Від більшого до меншого", "Від меншого до більшого")

        binding.sortFieldSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fieldOptions)
        binding.sortOrderSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, orderOptions)

        binding.sortFieldSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sortClients(adapter)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.sortOrderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sortClients(adapter)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun sortClients(adapter: ClientAdapter) {
        val selectedField = binding.sortFieldSpinner.selectedItem.toString()
        val selectedOrder = binding.sortOrderSpinner.selectedItem.toString()

        val comparator = when (selectedField) {
            "Кількість замовлень" -> compareBy<Client> { it.orders }
            "Сума витрат" -> compareBy<Client> { it.price }
            else -> compareBy<Client> { it.orders }
        }

        if (selectedOrder == "Від більшого до меншого") {
            filteredClients.sortWith(comparator.reversed())
        } else {
            filteredClients.sortWith(comparator)
        }

        adapter.notifyDataSetChanged()
        updateEmptyViewVisibility()
    }

    private fun updateEmptyViewVisibility() {
        if (filteredClients.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }
}
