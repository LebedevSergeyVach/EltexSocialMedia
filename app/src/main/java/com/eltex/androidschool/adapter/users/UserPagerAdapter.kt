package com.eltex.androidschool.adapter.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.adapter.events.EventAdapter
import com.eltex.androidschool.adapter.job.JobAdapter
import com.eltex.androidschool.adapter.posts.PostAdapter

import com.eltex.androidschool.databinding.LayoutPostListBinding
import com.eltex.androidschool.databinding.LayoutEventListBinding
import com.eltex.androidschool.databinding.LayoutJobListBinding
import com.eltex.androidschool.ui.common.OffsetDecoration
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallViewModel
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallMessage

/**
 * Адаптер для управления вкладками с постами, событиями и местами работы в `ViewPager2`.
 * Используется для отображения списка постов, событий и мест работы пользователя.
 *
 * @property postAdapter Адаптер для отображения списка постов.
 * @property eventAdapter Адаптер для отображения списка событий.
 * @property jobAdapter Адаптер для отображения списка мест работы.
 * @property offset Величина отступа между элементами в `RecyclerView`.
 * @property viewModel ViewModel для управления состоянием постов.
 * @see RecyclerView.Adapter
 */
class UserPagerAdapter(
    private val postAdapter: PostAdapter,
    private val eventAdapter: EventAdapter,
    private val jobAdapter: JobAdapter,
    private val offset: Int,
    private val adapter: PostAdapter,
    private val viewModel: PostWallViewModel,
) : RecyclerView.Adapter<UserPagerAdapter.ViewHolder>() {

    /**
     * Внутренний класс, представляющий ViewHolder для `RecyclerView`.
     * Используется для хранения ссылок на элементы интерфейса.
     *
     * @property itemView Корневой View элемента списка.
     * @see RecyclerView.ViewHolder
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * Создает и возвращает `ViewHolder` для соответствующей вкладки (посты, события или места работы).
     *
     * @param parent Родительский контейнер, в который будет добавлен ViewHolder.
     * @param viewType Тип ViewHolder (0 — посты, 1 — события, 2 — места работы).
     * @return [ViewHolder] для соответствующей вкладки.
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = LayoutPostListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                binding.postsRecyclerView.addOnChildAttachStateChangeListener(
                    object : RecyclerView.OnChildAttachStateChangeListener {
                        override fun onChildViewAttachedToWindow(view: View) {
                            val itemsCount = adapter.itemCount
                            val adapterPosition =
                                binding.postsRecyclerView.getChildAdapterPosition(view)

                            if (itemsCount - 2 <= adapterPosition) {
                                viewModel.accept(message = PostWallMessage.LoadNextPage)
                            }
                        }

                        override fun onChildViewDetachedFromWindow(view: View) = Unit
                    }
                )

                binding.postsRecyclerView.adapter = postAdapter
                binding.postsRecyclerView.layoutManager = LinearLayoutManager(parent.context)
                binding.postsRecyclerView.addItemDecoration(OffsetDecoration(offset))

                postAdapter.registerAdapterDataObserver(object :
                    RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        checkIfEmpty(binding)
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        checkIfEmpty(binding)
                    }

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        checkIfEmpty(binding)
                    }
                })

                checkIfEmpty(binding)

                ViewHolder(binding.root)

            }

            1 -> {
                val binding = LayoutEventListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.eventsRecyclerView.adapter = eventAdapter
                binding.eventsRecyclerView.layoutManager = LinearLayoutManager(parent.context)
                binding.eventsRecyclerView.addItemDecoration(OffsetDecoration(offset))

                eventAdapter.registerAdapterDataObserver(object :
                    RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        checkIfEmpty(binding)
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        checkIfEmpty(binding)
                    }

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        checkIfEmpty(binding)
                    }
                })

                checkIfEmpty(binding)

                ViewHolder(binding.root)
            }

            2 -> {
                val binding = LayoutJobListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                binding.jobsRecyclerView.adapter = jobAdapter
                binding.jobsRecyclerView.layoutManager = LinearLayoutManager(parent.context)
                binding.jobsRecyclerView.addItemDecoration(OffsetDecoration(offset))

                jobAdapter.registerAdapterDataObserver(object  :
                RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        checkIfEmpty(binding)
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        checkIfEmpty(binding)
                    }

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        checkIfEmpty(binding)
                    }
                })

                checkIfEmpty(binding)

                ViewHolder(binding.root)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    /**
     * Проверяет, пуст ли список постов, и управляет видимостью `RecyclerView` и `TextView` с сообщением "Постов пока нет".
     * Добавляет анимацию для плавного перехода между состояниями.
     *
     * @param binding Привязка для макета списка постов.
     * @see RecyclerView.AdapterDataObserver
     */
    private fun checkIfEmpty(binding: LayoutPostListBinding) {
        val isEmpty = postAdapter.itemCount == 0

        if (isEmpty) {
            binding.emptyPostsText.visibility = View.VISIBLE
            binding.emptyPostsText.alpha = 0f
            binding.emptyPostsText.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            binding.postsRecyclerView.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.postsRecyclerView.visibility = View.GONE
                }
                .start()
        } else {
            binding.postsRecyclerView.visibility = View.VISIBLE
            binding.postsRecyclerView.alpha = 0f
            binding.postsRecyclerView.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            binding.emptyPostsText.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.emptyPostsText.visibility = View.GONE
                }
                .start()
        }
    }

    /**
     * Проверяет, пуст ли список событий, и управляет видимостью `RecyclerView` и `TextView` с сообщением "Событий пока нет".
     * Добавляет анимацию для плавного перехода между состояниями.
     *
     * @param binding Привязка для макета списка событий.
     * @see RecyclerView.AdapterDataObserver
     */
    private fun checkIfEmpty(binding: LayoutEventListBinding) {
        val isEmpty = eventAdapter.itemCount == 0

        if (isEmpty) {
            binding.emptyEventsText.visibility = View.VISIBLE
            binding.emptyEventsText.alpha = 0f
            binding.emptyEventsText.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            binding.eventsRecyclerView.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.eventsRecyclerView.visibility = View.GONE
                }
                .start()
        } else {
            binding.eventsRecyclerView.visibility = View.VISIBLE
            binding.eventsRecyclerView.alpha = 0f
            binding.eventsRecyclerView.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            binding.emptyEventsText.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.emptyEventsText.visibility = View.GONE
                }
                .start()
        }
    }

    /**
     * Проверяет, пуст ли список мест работы, и управляет видимостью `RecyclerView` и `TextView` с сообщением "Мест работы пока нет".
     *
     * @param binding Привязка для макета списка мест работы.
     * @see RecyclerView.AdapterDataObserver
     */
    private fun checkIfEmpty(binding: LayoutJobListBinding) {
        val isEmpty = jobAdapter.itemCount == 0

        if (isEmpty) {
            binding.emptyJobsText.visibility = View.VISIBLE
            binding.emptyJobsText.alpha = 0f
            binding.emptyJobsText.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            binding.jobsRecyclerView.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.jobsRecyclerView.visibility = View.GONE
                }
                .start()
        } else {
            binding.jobsRecyclerView.visibility = View.VISIBLE
            binding.jobsRecyclerView.alpha = 0f
            binding.jobsRecyclerView.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            binding.emptyJobsText.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.emptyJobsText.visibility = View.GONE
                }
                .start()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    /**
     * Возвращает количество вкладок (посты, события и места работы).
     *
     * @return Количество вкладок (3).
     * @see RecyclerView.Adapter.getItemCount
     */
    override fun getItemCount(): Int = 3

    /**
     * Возвращает тип ViewHolder для соответствующей позиции.
     *
     * @param position Позиция вкладки.
     * @return 0 для постов, 1 для событий, 2 для мест работы.
     * @see RecyclerView.Adapter.getItemViewType
     */
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            1 -> 1
            2 -> 2
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
